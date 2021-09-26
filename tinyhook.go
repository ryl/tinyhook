package main

import (
  "bytes"
  "crypto/hmac"
  "crypto/sha256"
  "encoding/hex"
  "fmt"
  // "html"
  // "io"
  "io/ioutil"
  "log"
  "net/http"
  "encoding/json"
  "os/exec"
  "os"
)

type Config struct {
  Port int
  SSLCertificate string
  SSLPrivateKey string
  Hooks []HookConfig
}

type HookConfig struct {
  Hook string
  Script string
  GitHubSecretToken string
}

var config Config

func main() {
  // config.json is the main configuration file for the application.
  // It contains server configuration and maps URLS to scripts.
  data, err := ioutil.ReadFile(os.Args[1])
  if err != nil {
    log.Fatal(err)
  }

  err = json.Unmarshal(data, &config)
  if err != nil {
    log.Fatal(err)
  }

  if config.Hooks == nil || len(config.Hooks) == 0 {
    log.Fatal("You must configure some hooks in config.json.")
  }

  // Configure a greeting.
  http.HandleFunc("/", tinyhookGreeting)

  // Hooks are configured by mapping a URL to a script to run.
  for _, hook := range config.Hooks {
    fmt.Printf("Configured hook \"%v\" -> \"%v\"\n", hook.Hook, hook.Script)
    hookCopy := hook
    http.HandleFunc(hook.Hook, func(w http.ResponseWriter, r *http.Request) {
      hookHandler(w, r, hookCopy)
    })
  }

  // Last but not least, start listening
  addr := fmt.Sprintf(":%v", config.Port)
  fmt.Println("Starting on", addr)
  log.Fatal(http.ListenAndServeTLS(
    addr,
    config.SSLCertificate,
    config.SSLPrivateKey,
    nil))
  //log.Fatal(http.ListenAndServe(addr, nil))
}

func tinyhookGreeting(w http.ResponseWriter, r *http.Request) {
  fmt.Fprintf(w, "This is tinyhook reporting for duty!")
}

func hookHandler(w http.ResponseWriter, r *http.Request, hook HookConfig) {
  // fmt.Println(hook)
  // fmt.Println("Method:", r.Method)
  // fmt.Println("Body:")

  // Read in the body of the request
  b, e := ioutil.ReadAll(r.Body)
  if e != nil {
    log.Fatal(e)
  }
  // body := string(b)
  // fmt.Println(body)

  // Validate the request signature if a secret token is provided.
  // This is an important step to securing a webhook.
  if hook.GitHubSecretToken != "" {
    if !validateGitHubSignature(
          b,
          []byte(r.Header.Get("X-Hub-Signature-256")),
          []byte(hook.GitHubSecretToken)) {
      return
    }
  }

  // Run local commands and print their output.
  var out bytes.Buffer
  cmd := exec.Command(hook.Script, string(b))
  cmd.Stdout = &out
  err := cmd.Run()

  if err != nil {
    log.Println(err)
    return
  }
  // Print messages to the console and request.
  fmt.Printf(out.String())
  //fmt.Fprintf(w, "Hello, %q\n", html.EscapeString(r.URL.Path))
}

func validateGitHubSignature(message, messageMAC, secretToken []byte) bool {
  mac := hmac.New(sha256.New, secretToken)
  mac.Write(message)
  expectedMAC := mac.Sum(nil)
  hexMac := fmt.Sprintf("sha256=%v", hex.EncodeToString(expectedMAC))
  return hmac.Equal(messageMAC, []byte(hexMac))
}
