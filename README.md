# TS James
TS James is the name for a network of several standalone programs, managing and especially _connecting¹_ users over supported social plattform services.
Currently the support of following servies is planned:
* TeamSpeak³ Server (bot for selected server)
* Discord Server (bot for selected server)
* Steam Bot (bot with Steam friendships to the user)
* ...

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/d361f3474eaf4e98a7dd636182dd05aa)](https://app.codacy.com/app/PSandro/TSJames?utm_source=github.com&utm_medium=referral&utm_content=PSandro/TSJames&utm_campaign=Badge_Grade_Settings)[![Build Status](https://travis-ci.org/PSandro/TSJames.svg?branch=master)](https://travis-ci.org/PSandro/TSJames)

# Features
  footnote ¹: An user will be able to see if any of his friends are online and if so, on which services they are active. Furthermore an user will be able to interact with other registered Users over the TS James network.
### Planned Features
  - user registration and management to external database
  - user verification with automated rank promotion
  - music-bot subscription by certain users
  - user interaction:
    * chat
    * shortcut commands (for example "meet John ts" -> TS James will notify John that the requested user wants to meet him on ts)
    * online status
  
# TODO
  - implement TS3 API
  - implement Discord API
  - build networking via Netty
  - build User Web Interface
