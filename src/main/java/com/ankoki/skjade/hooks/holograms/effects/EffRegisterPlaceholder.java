package com.ankoki.skjade.hooks.holograms.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.function.ExprFunctionCall;
import ch.njol.skript.lang.function.FunctionReference;
import ch.njol.skript.util.Timespan;
import ch.njol.util.Kleenean;
import com.ankoki.skjade.SkJade;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

/**
 * @author Mr. Darth c: tyty for da help w function parsing<3
 */
@Name("Register Placeholder")
@Description({"Registers a placeholder for HolographicDisplays only.",
              "IMPORTANT: If a hologram is created before a placeholder is registered, you need to recreate the placeholder."})
@Examples("register placeholder \"onlinePlayers\" to run funcThatReturnsString()")
@RequiredPlugins("HolographicDisplays")
@Since("1.0.0, specifying a refresh rate since 1.4.1.")
public class EffRegisterPlaceholder extends Effect {

    static {
        Skript.registerEffect(EffRegisterPlaceholder.class,
                "register [[a] holo[graphic[ displays]]] placeholder %string% to run [[the] function] <(.+)>\\([<.*?>]\\)",
                "register [[a] holo[graphic[ displays]]] placeholder %string% to run [[the] function] <(.+)>\\([<.*?>]\\) (with|at) [a] [refresh] rate of %timespan%");
    }

    private Expression<String> textExpr;
    private Expression<Timespan> timespanExpr;
    private ExprFunctionCall functionCall;

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, ParseResult parseResult) {
        textExpr = (Expression<String>) exprs[0];
        timespanExpr = i == 1 ?(Expression<Timespan>) exprs[1] : null;
        String unparsed = parseResult.regexes.get(0).group(0) + "(" + (parseResult.regexes.size() > 1 ? parseResult.regexes.get(1).group(0) : "") + ")";
        FunctionReference<?> function = new SkriptParser(unparsed, SkriptParser.ALL_FLAGS, ParseContext.DEFAULT)
                .parseFunction((Class<?>[]) null);
        if (function == null) {
            Skript.error("This isn't a valid function! Your function needs to return a value!");
            return false;
        }
        functionCall = new ExprFunctionCall(function);
        functionCall.getReturnType();
        return true;
    }

    @Override
    protected void execute(Event event) {
        String text = textExpr.getSingle(event);
        Timespan timespan = timespanExpr == null ? Timespan.fromTicks_i(20) : timespanExpr.getSingle(event);
        if (text == null || timespan == null) return;
        double rate = Double.max(20, timespan.getTicks_i()) / 20;
        HologramsAPI.registerPlaceholder(SkJade.getInstance(), text, rate,
                () -> functionCall.getArray(event)[0] == null ? "" : ((String) functionCall.getArray(event)[0]).replaceAll(".$", ""));
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "register holographic displays placeholder " + textExpr.toString(event, b) + " to run " + functionCall.toString(event, b);
    }
}
