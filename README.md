# 2B2T: Bot Ignore

## Introduction

A minecraft mod to auto-ignore bots.  This mod will fetch an updated list every 5 minutes of known bots to ignore.  The object of the master list is to stop spam from useless bots.  Established bots that everyone likes are not in the list.  If you do not like my list, you can use your own.  You need to rebuild the mod with a url link to a text file which contains Minecraft user UUIDs.

## Usage

This mod requires Minecraft Forge

Release package
1. Drop the released .jar into your minecraft mod directory


Build
```
git clone https://github.com/CraftyChaos/2b2t-bot-ignore.git
cd 2b2t-bot-ignore

# to edit it eclipse
./gradlew eclipse

# to build
./gradlew build

cp build/libs/botignore.* <path to mineraft mod directory>
```


