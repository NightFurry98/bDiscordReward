# bDiscordReward
This is a simple plugin for a minecraft server that allows you to create a discord bot that gives rewards for joining your server.

# Instruction
  * Configure MySQL Database Connection:
      Open the config.yml file.
      Enter the necessary information for connecting to your MySQL database.

 * Create Your Discord Bot:
      Visit the official [Discord Developer Portal](https://discord.com/developers/applications) to create your bot.
      Follow the instructions on the website to set up your bot.

  * Enter the Bot’s Authorization Token:
      Copy your bot's authorization token from the Discord Developer Portal.
      Paste the token into the config.yml file, under the appropriate section.
    
  * Start Your Server:
     Once everything is configured, launch your server to begin running the bot and connecting to the database.

# Commands
  * [MINECRAFT] /bdsicordreward - managing the configuration file, adding items to rewards
  * [DISCORD] /rewardchannel - creating a message on the discord channel where you will be able to receive the rewrd

Spigot version: 1.16.5+ 

Please report any bugs [here](https://github.com/NightFurry98/bDiscordReward/issues) or via Discord messages to: bonappetit_

# Maven
Repository:
```xml
<repository>
  <id>jitpack.io</id>
  <url>https://jitpack.io</url>
</repository>
  ```
Dependency:
```xml
<dependency>
    <groupId>com.github.NightFurry98</groupId>
    <artifactId>bDiscordReward</artifactId>
    <version>v0.8.1</version>
</dependency>
```

## Available Methods
 *  RewardAPI.canReceiveReward(String nick)
```java
 boolean canReceive = RewardAPI.canReceiveReward("Player123");
 ```
 *  RewardAPI.getRewardCount(String nick)
```java
int rewardCount = RewardAPI.getRewardCount("Player123");
```
 *  RewardAPI.getNextRewardTime(String nick)
```java
Timestamp nextReward = RewardAPI.getNextRewardTime("Player123");
```
