package rp.folkevognen.listeners;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import rp.folkevognen.Interaction;
import rp.folkevognen.Settings;

public class UserContextInteractionListener extends ListenerAdapter {
    Logger logger = LoggerFactory.getLogger(UserContextInteractionListener.class);
    @Override
    public void onUserContextInteraction(UserContextInteractionEvent event) {
        logger.info(event.getName());
        for (Interaction<UserContextInteractionEvent> interaction : interactions) {
            if (event.getName().equals(interaction.name)) {
                interaction.function.run(event);
            }
        }
    }

    public static List<Interaction<UserContextInteractionEvent>> interactions = new ArrayList<>(){{
        add(new Interaction<>(
            "set-folker",
            "Set the folker",
            Command.Type.USER,
            e -> {
                // decrement the previous folker and increment the new one
                Settings settings = new Settings();
                settings.folkevognen.put(settings.lastFolker, settings.folkevognen.get(settings.lastFolker) - 1);
                settings.folkevognen.put(e.getUser().getId(), settings.folkevognen.get(e.getUser().getId()) + 1);
                settings.lastFolker = e.getUser().getId();
                settings.write();
                e.reply("Folker set, please refresh the folkevognen").setEphemeral(true).queue();
            }
        ));
    }};
}
