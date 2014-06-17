package jp.gecko655.fujimiya.bot;

import static org.junit.Assert.*;

import org.junit.Test;

public class Fujimiya {

    @Test
    public void test() {
        FujimiyaBot bot = new FujimiyaBot();
        bot.getFujimiyaUrl("藤宮さん");
        //System.out.println(tweet);
    }

}
