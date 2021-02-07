package com.ankoki.skjade.elements.pastebinapi.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.ankoki.skjade.elements.pastebinapi.PasteManager;
import com.besaba.revonline.pastebinapi.paste.PasteBuilder;
import com.besaba.revonline.pastebinapi.paste.PasteExpire;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

@Name("Set Paste to Expire")
@Description("Set when a paste will expire.")
@Examples("set the paste with the id \"myPaste\" to never expire")
@Since("1.0.0")
public class EffSetExpire extends Effect {

    static {
        Skript.registerEffect(EffSetExpire.class,
                "set %pastes% to never expire",
                "set %pastes% to expire in (a|1|one) month",
                "set %pastes% to expire in (2|two) weeks",
                "set %pastes% to expire in (a|1|one) week",
                "set %pastes% to expire in (a|1|one) day",
                "set %pastes% to expire in (an|1|one) hour",
                "set %pastes% to expire in (ten|10) minutes");
    }

    private Expression<PasteBuilder> paste;
    private PasteExpire expires;

    @Override
    protected void execute(Event e) {
        PasteBuilder[] builder = paste.getArray(e);
        if (builder.length > 0 || expires != null) {
            Arrays.stream(builder).forEach(b -> PasteManager.setExpires(b, expires));
        }
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "set " + paste.toString(e, debug) + " to expire";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        switch (matchedPattern) {
            case 0:
                expires = PasteExpire.Never;
                break;
            case 1:
                expires = PasteExpire.OneMonth;
                break;
            case 2:
                expires = PasteExpire.TwoWeek;
                break;
            case 3:
                expires = PasteExpire.OneWeek;
                break;
            case 4:
                expires = PasteExpire.OneDay;
                break;
            case 5:
                expires = PasteExpire.OneHour;
                break;
            case 6:
                expires = PasteExpire.TenMinutes;
        }
        paste = (Expression<PasteBuilder>) exprs[0];
        return true;
    }
}
