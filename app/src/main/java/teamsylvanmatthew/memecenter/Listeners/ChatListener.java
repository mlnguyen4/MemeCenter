package teamsylvanmatthew.memecenter.Listeners;

import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.types.GenericMessageEvent;


public class ChatListener extends ListenerAdapter {

    @Override
    public void onGenericMessage(final GenericMessageEvent event) throws Exception {

        //When someone says ?helloworld respond with "Hello World"
        if (event.getMessage().startsWith("?helloworld"))
            event.respond("Hello world!");
    }

}
