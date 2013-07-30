package com.ouchadam.fang.parsing.helper;

import com.ouchadam.fang.domain.channel.Image;
import com.ouchadam.fang.domain.item.Audio;

public class cnet_small implements XmlValues {

    @Override
    public String channelTitle() {
        return "CNET UK Podcast";
    }

    @Override
    public String channelCategory() {
        return "Technology";
    }

    @Override
    public Image channelImage() {
        return new Image("http://www.cnet.co.uk/images/rss/logo-cnet.jpg", "http://crave.cnet.co.uk/podcast/", "CNET UK Podcast", 88, 56);
    }

    @Override
    public String channelSummary() {
        return "\n" +
                "      Britain's best technology podcast is beamed to your auditory sensors every Friday afternoon direct from CNET UK.\n" +
                "      The honey-toned team give you everything you need to know about the week's hottest tech news, the most\n" +
                "      Crave-worthy new gadgets and answer your best questions and funniest comments. There's a special feature every\n" +
                "      week, in which we tackle a burning tech topic in more detail, and lots more fun besides. Subscribe now, and let us\n" +
                "      know what you think!\n" +
                "    ";
    }

    @Override
    public int itemCount() {
        return 5;
    }

    @Override
    public String firstItemTitle() {
        return "UK government blocks porn in Podcast 349";
    }

    @Override
    public String firstItemLink() {
        return "\n" +
                "        http://crave.cnet.co.uk/podcast/uk-government-blocks-porn-in-podcast-349-50011796/\n" +
                "      ";
    }

    @Override
    public String firstItemPubDate() {
        return "Thu, 25 Jul 2013 11:59:02 +0100";
    }

    @Override
    public Audio firstItemAudio() {
        return new Audio("http://www.podtrac.com/pts/redirect.mp3?http://cdn-media.cbsinteractive.co.uk/cnetcouk/podcasts/crave/cnetuk_podcast_349.mp3", "audio/mpeg");
    }

    @Override
    public String firstItemSubtitle() {
        return "UK government blocks porn in Podcast 349";
    }

    @Override
    public String firstItemSummary() {
        return "\n" +
                "        The government wants to block online porn -- but will it work? Down with this sort of thing!\n" +
                "      ";
    }
}
