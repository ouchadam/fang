package com.ouchadam.fang.parsing.helper;

import com.ouchadam.fang.FangCalendar;
import com.ouchadam.fang.domain.channel.Image;
import com.ouchadam.fang.domain.item.Audio;

import java.util.List;

public class cnet_small implements XmlValues {

    @Override
    public String channelTitle() {
        return "CNET UK Podcast";
    }

    @Override
    public List<String> channelCategory() {
        return Util.categoryList("Technology", "Gadgets", "Tech News");
    }

    @Override
    public Image channelImage() {
        return new Image("http://www.cnet.co.uk/images/rss/logo-cnet.jpg", "http://crave.cnet.co.uk/podcast/", "CNET UK Podcast", 88, 56);
    }

    @Override
    public String channelSummary() {
        return "Britain's best technology podcast is beamed to your auditory sensors every Friday afternoon direct from CNET UK. The honey-toned team give you everything you need to know about the week's hottest tech news, the most Crave-worthy new gadgets and answer your best questions and funniest comments. There's a special feature every week, in which we tackle a burning tech topic in more detail, and lots more fun besides. Subscribe now, and let us know what you think!";
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
        return "http://crave.cnet.co.uk/podcast/uk-government-blocks-porn-in-podcast-349-50011796/";
    }

    @Override
    public FangCalendar firstItemPubDate() {
        return new FangCalendar("Thu, 25 Jul 2013 11:59:02 +0100");
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
        return "The government wants to block online porn -- but will it work? Down with this sort of thing!";
    }
}
