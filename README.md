<div align="center">
  <img src="https://i.ibb.co/th0m4Mq/logo.png" width="20%">
  <img src="https://i.ibb.co/sFw2p8j/shopping.png" width="10%">
</div>

<h2>About</h2>
<p>
  Price watcher and notifier on Nike, Zalando, Zara, House, Hebe. The project handles user registration, login and JWT Tokens. Passwords are encrypted using BCrypt.
  <br>
  <b>Playwright</b> is used for web scraping. Web scraping includes product variant, product availbility etc.
  <br>
  If product is cheaper Pricety notifies immediately on user telegram chat and user email.
  <br>
  User can store multiple products on their account, edit price alerts, titles, descriptions etc.
</p>

<h2>Live</h2>
<h4>
  <a href="https://pricety.onrender.com">Pricety on website</a>
</h4>
<h4>
  <a href="https://t.me/zalandoscraperbot">Pricety on telegram</a>
</h4>

<h2>Preview</h2>
<div>
  <img src="https://i.ibb.co/RhvfWX9/Zrzut-ekranu-2024-04-25-o-14-22-21.png" width="45%">
  <img src="https://i.ibb.co/g69wHVL/Zrzut-ekranu-2024-04-25-o-20-46-22.png" width="45%">
  <div align="center" width="100%">
      <img src="https://i.ibb.co/3m54DPQ/Zrzut-ekranu-2024-04-25-o-14-30-45.png" width="45%" align="center">
  </div>
</div>

<h2>Usage</h2>
<h3>What you need?</h3>
<p><b>1.</b> Gmail address to nofity users</p>
<p><b>2.</b> SMTP password for gmail address</p>
<p><b>3.</b> Telegram bot token <a href="https://t.me/botfather">here</a></p>
<p><b>4.</b> 256-bit encryption key for JWT Tokens <a href="https://asecuritysite.com/encryption/plain">generate here</a></p>
<p><b>5.</b> Database password</p>
<p><b>6.</b> Database URL</p>
<p><b>7.</b> Database with tables, use this queries: <a href="https://github.com/vicaryy/Pricety/blob/master/src/main/resources/database-query.txt"></p>
<p><b>8.</b> <a href="https://www.docker.com/">Docker</a> on your machine</p>

<br>

<h3>It's time for Docker.</h3>

<p>Go to Pricety folder where Dockerfile is located and open <b>terminal</b> there. Then run this command to build project:</p>

```sh
docker build -t pricety:1.0.0 .
```

<p>Then run this command to run project:</p>

```sh
docker run -p 8080:8080 -e BOT_TOKEN='[bot token]' -e SECRET_KEY='[256-bit encryption key]' -e DB_PASSWORD='[db password]' -e DB_URL='[db url]' -e MAIL_PASSWORD='[smtp email password]' -e MAIL_ADDRESS='[email address]' pricety:1.0.0
```

<h3>And voilà, Pricety is running on your machine.</h3>
<br>

<h2>Configuration</h2>
<h3>Database</h3>
<p>Pricety is waiting for the JSON files of an entities to the database. Without it, it will not function properly.</p>
<br>

<p>If you <b>DON'T</b> want to add any records to database then send this to your server:</p>

```sh
FOR EVERY REQUEST YOU HAVE TO SET THIS HEADER: ("secretKey", [256-bit encryption key])

POST MAPPING
/api/set/done
```
<br>

<p>If you <b>WANT</b> to add records to database then send this to your server:</p>

```sh
FOR EVERY REQUEST YOU HAVE TO SET THIS HEADER: ("secretKey", [256-bit encryption key])

POST MAPPINGS
/api/set/users - JSON file of List<UserEntity>
/api/set/products - JSON file of List<ProductEntity>
/api/set/activeRequests - JSON file of List<ActiveRequestEntity>
/api/set/awaitedMessages - JSON file of List<AwaitedMessageEntity>
/api/set/dataImports - JSON file of List<DataImportEntity>
/api/set/emailVerifications - JSON file of List<EmailVerificationEntity>
/api/set/linkRequests - JSON file of List<LinkRequestEntity>
/api/set/messages - JSON file of List<MessageDTO>
/api/set/notificationChats - JSON file of List<NotificationChatEntity>
/api/set/notificationEmails - JSON file of List<NotificationEmailEntity>
/api/set/productHistories - JSON file of List<ProductHistoryEntity>
/api/set/waitingUsers - JSON file of List<WaitingUserDTO>
```

<br>

<h2>API</h2>
<h4>If you want to use admin commands you have to set one user as admin in your database(user_test table)</h4>

<h3>Web API</h3>

```sh
FOR EVERY REQUEST YOU HAVE TO SET THIS HEADER: ("secretKey", [256-bit encryption key])

GET MAPPINGS
/api/get/users - returns JSON file of List<UserEntity>
/api/get/products - returns JSON file of List<ProductEntity>
/api/get/activeRequests - returns JSON file of List<ActiveRequestEntity>
/api/get/awaitedMessages - returns JSON file of List<AwaitedMessageEntity>
/api/get/dataImports - returns JSON file of List<DataImportEntity>
/api/get/emailVerifications - returns JSON file of List<EmailVerificationEntity>
/api/get/linkRequests - returns JSON file of List<LinkRequestEntity>
/api/get/messages - returns JSON file of List<MessageDTO>
/api/get/notificationChats - returns JSON file of List<NotificationChatEntity>
/api/get/notificationEmails - returns JSON file of List<NotificationEmailEntity>
/api/get/productHistories - returns JSON file of List<ProductHistoryEntity>
/api/get/waitingUsers - returns JSON file of List<WaitingUserDTO>
```
<br>

<h3>Telegram API</h3>

```sh
Set:
//set premium <userId> - setting user to premium
//set standard <userId> - setting user to standard
//set admin <userId> - setting user to admin
//set non-admin <userId> - setting user to non-admin
//set command <command:description> - setting command to telegram menu block
//set nick <userId> <newNick> - setting new nick to user

Delete:
//delete command <command> - deleting command
//delete command all - deleting all commands
//delete user <userId> - deleting user and all of his products
//delete product <productId> - deleting product

Ban:
//ban userId  -  not yet

Update:
//update start - app going to update products for every 3 hours
//update start once - app going to update product only one time
//update stop - stopping //update start updating
//update state - check current update state (is running? is stopped?)

Send:
//send message all <text> - send message to all users
//send message all <PL> -en- <ENG> - send message to all users in Polish and English language
//send message <userId> <text> - send message to one user

Get:
//get user all - display all users
//get user <userId> - display one user
//get product all - display all products
//get product <productId> - display one product
//get productUser <userId> - display all product of one user
//get all - display all commands

Application:
//start - starting if app was stopped
//stop - stopping app
//crash - crashing app, can't start again
```
<br>

## Author

👤 **vicary**

## Want to support me?

Give a ⭐️ if this project helped you!

---






