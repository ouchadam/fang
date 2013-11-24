package com.ouchadam.fang.presentation.topten;

public enum TopTenType {
    ALL {
        @Override
        public String getUrl() {
            return "https://itunes.apple.com/us/rss/toppodcasts/limit=10/explicit=true/xml";
        }
    },
    ARTS {
        @Override
        public String getUrl() {
            return "https://itunes.apple.com/us/rss/toppodcasts/limit=10/genre=1301/explicit=true/xml";
        }
    },
    BUSINESS {
        @Override
        public String getUrl() {
            return "https://itunes.apple.com/us/rss/toppodcasts/limit=10/genre=1321/explicit=true/xml";
        }
    },
    COMEDY {
        @Override
        public String getUrl() {
            return "https://itunes.apple.com/us/rss/toppodcasts/limit=10/genre=1303/explicit=true/xml";
        }
    },
    EDUCATION {
        @Override
        public String getUrl() {
            return "https://itunes.apple.com/us/rss/toppodcasts/limit=10/genre=1304/explicit=true/xml";
        }
    },
    GAMES_HOBBIES {
        @Override
        public String getUrl() {
            return "https://itunes.apple.com/us/rss/toppodcasts/limit=10/genre=1323/explicit=true/xml";
        }
    },
    GOVERNMENT {
        @Override
        public String getUrl() {
            return "https://itunes.apple.com/us/rss/toppodcasts/limit=10/genre=1325/explicit=true/xml";
        }
    },
    HEALTH {
        @Override
        public String getUrl() {
            return "https://itunes.apple.com/us/rss/toppodcasts/limit=10/genre=1307/explicit=true/xml";
        }
    },
    KIDS_FAMILY {
        @Override
        public String getUrl() {
            return "https://itunes.apple.com/us/rss/toppodcasts/limit=10/genre=1305/explicit=true/xml";
        }
    },
    MUSIC {
        @Override
        public String getUrl() {
            return "https://itunes.apple.com/us/rss/toppodcasts/limit=10/genre=1310/explicit=true/xml";
        }
    },
    NEWS_POLITICS {
        @Override
        public String getUrl() {
            return "https://itunes.apple.com/us/rss/toppodcasts/limit=10/genre=1311/explicit=true/xml";
        }
    },
    RELIGION_SPIRITUALITY {
        @Override
        public String getUrl() {
            return "https://itunes.apple.com/us/rss/toppodcasts/limit=10/genre=1314/explicit=true/xml";
        }
    },
    SCIENCE_MEDICINE {
        @Override
        public String getUrl() {
            return "https://itunes.apple.com/us/rss/toppodcasts/limit=10/genre=1315/explicit=true/xml";
        }
    },
    SOCIETY_CULTURE {
        @Override
        public String getUrl() {
            return "https://itunes.apple.com/us/rss/toppodcasts/limit=10/genre=1324/explicit=true/xml";
        }
    },
    SPORTS_REC {
        @Override
        public String getUrl() {
            return "https://itunes.apple.com/us/rss/toppodcasts/limit=10/genre=1316/explicit=true/xml";
        }
    },
    TV_FILM {
        @Override
        public String getUrl() {
            return "https://itunes.apple.com/us/rss/toppodcasts/limit=10/genre=1309/explicit=true/xml";
        }
    },
    TECHNOLOGY {
        @Override
        public String getUrl() {
            return "https://itunes.apple.com/us/rss/toppodcasts/limit=10/genre=1318/explicit=true/xml";
        }
    };

    public abstract String getUrl();

}
