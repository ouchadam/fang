CREATE TABLE IF NOT EXISTS "CHANNEL" ("_id" INTEGER PRIMARY KEY NOT NULL, "CHANNEL_TITLE" TEXT NOT NULL UNIQUE, "CATEGORY" TEXT, "SUMMARY" TEXT, "URL" TEXT);
CREATE TABLE IF NOT EXISTS "IMAGE" ("_id" INTEGER PRIMARY KEY NOT NULL, "CHANNEL" TEXT NOT NULL, "TITLE" TEXT, "URL" TEXT UNIQUE, "LINK" TEXT, "WIDTH" INTEGER, "HEIGHT" INTEGER);
CREATE TABLE IF NOT EXISTS "ITEM" ("_id" INTEGER PRIMARY KEY NOT NULL, "CHANNEL" TEXT NOT NULL, "TITLE" TEXT NOT NULL UNIQUE, "PUBDATE" LONG, "DURATION" TEXT, "AUDIO_URL" TEXT, "AUDIO_TYPE" TEXT, "LINK" TEXT, "HERO_IMAGE" TEXT, "SUBTITLE" TEXT, "SUMMARY" TEXT);
CREATE TABLE IF NOT EXISTS "PLAYLIST" ("_id" INTEGER PRIMARY KEY NOT NULL, "ITEM_ID" INTEGER NOT NULL UNIQUE, INTEGER, "DOWNLOADED" INTEGER, "DOWNLOAD_ID" LONG, "PLAY_POSITION" INTEGER, "MAX_DURATION" INTEGER);

CREATE VIEW IF NOT EXISTS "FULL_CHANNEL" AS SELECT * FROM CHANNEL LEFT OUTER JOIN IMAGE ON CHANNEL.CHANNEL_TITLE = IMAGE.CHANNEL;
CREATE VIEW IF NOT EXISTS "FULL_ITEM" AS SELECT * FROM ITEM LEFT OUTER JOIN IMAGE ON ITEM.CHANNEL = IMAGE.CHANNEL LEFT OUTER JOIN PLAYLIST ON ITEM._id = PLAYLIST.ITEM_ID LEFT OUTER JOIN CHANNEL ON ITEM.CHANNEL = CHANNEL.CHANNEL_TITLE;
CREATE VIEW IF NOT EXISTS "PLAYLIST_ITEM" AS SELECT * FROM PLAYLIST, ITEM WHERE ITEM_ID = ITEM._id;
