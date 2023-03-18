# ChatGPTBot
一个简易的接入ChatGPT的机器人，使用Java实现。  
目前还只是个调用api的demo，不过可以开箱即用。  
## 使用方式
只需要将chatgpt.apiKey配置成您的apiKey，就可以实现连续对话的功能了。  
启动后，POST请求localhost:8080/chat即可：

````
curl --location --request POST 'http://localhost:8080/chat' \
--header 'Content-Type: application/json' \
--data-raw '{"userName":"xiaobai","content":"你是谁"}'

````


后期会不断地丰富功能。
