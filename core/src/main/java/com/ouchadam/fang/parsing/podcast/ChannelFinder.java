package com.ouchadam.fang.parsing.podcast;

import com.novoda.sexp.finder.ElementFinder;
import com.novoda.sexp.finder.ElementFinderFactory;
import com.novoda.sexp.marshaller.BodyMarshaller;
import com.ouchadam.fang.domain.channel.Channel;

public class ChannelFinder {

    public static ElementFinder<Channel> newInstance() {
        ElementFinderFactory finderFactory = new ElementFinderFactory() {
            @Override
            public ElementFinder<String> getStringFinder() {
                return this.getTypeFinder(new BodyMarshaller<String>() {
                    @Override
                    public String marshall(String input) {
                        return removeWhitespace(input);
                    }

                    private String removeWhitespace(String input) {
                        String output = input.replaceAll("\n", "");
                        output = output.replaceAll("\t", "");
                        output = output.replaceAll("\\p{Z}", " ");
                        output = output.replaceAll("\\s+", " ");
                        return output.trim();
                    }
                });
            }
        };

        return finderFactory.getTypeFinder(new ChannelParser(finderFactory));
    }

}
