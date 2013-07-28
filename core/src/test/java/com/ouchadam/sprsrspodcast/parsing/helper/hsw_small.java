package com.ouchadam.sprsrspodcast.parsing.helper;

import com.ouchadam.sprsrspodcast.domain.channel.Image;
import com.ouchadam.sprsrspodcast.domain.item.Audio;

public class hsw_small implements XmlValues {

    @Override
    public String channelTitle() {
        return "Stuff You Should Know";
    }

    @Override
    public String channelCategory() {
        return "Society & Culture";
    }

    @Override
    public Image channelImage() {
        return new Image("\n" +
                "        http://podcasts.howstuffworks.com/hsw/podcasts/sysk/sysk-audio-1600.jpg\n" +
                "      ", "http://www.howstuffworks.com/", "Stuff You Should Know", 0, 0);
    }

    @Override
    public String channelSummary() {
        return "\n" +
                "      Why should you never scare a vulture? How do maps work? Join Josh and Chuck as they explore the Stuff You Should\n" +
                "      Know about everything from psychology to propellant in this podcast from HowStuffWorks.com.\n" +
                "    ";
    }

    @Override
    public int itemCount() {
        return 10;
    }

    @Override
    public String firstItemTitle() {
        return "How Maps Work";
    }

    @Override
    public String firstItemLink() {
        return "\n" +
                "        http://www.podtrac.com/pts/redirect.mp3/podcasts.howstuffworks.com/hsw/podcasts/sysk/2013-07-25-sysk-maps.mp3\n" +
                "      ";
    }

    @Override
    public String firstItemPubDate() {
        return "Thu, 25 Jul 2013 12:32:12 -0400";
    }

    @Override
    public Audio firstItemAudio() {
        return new Audio("http://www.podtrac.com/pts/redirect.mp3/podcasts.howstuffworks.com/hsw/podcasts/sysk/2013-07-25-sysk-maps.mp3", "audio/mpeg");
    }

    @Override
    public String firstItemSubtitle() {
        return "\n" +
                "        Yes, your brain may have just flash-dried from boredom at the thought of learning about maps, but it turns out\n" +
                "        they are a lot more than just tools for navigation. Maps are two-dimensional representations of how we imagine\n" +
                "        our world, with imagine being the operative world since every map in existence is riddled with errors.\n" +
                "      ";
    }

    @Override
    public String firstItemSummary() {
        return null;
    }
}
