/*    */ package net.minecraft.server.commands;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.Paths;
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.network.chat.ClickEvent;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.HoverEvent;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.network.chat.Style;
/*    */ import net.minecraft.util.profiling.jfr.Environment;
/*    */ import net.minecraft.util.profiling.jfr.JvmProfiler;
/*    */ 
/*    */ public class JfrCommand {
/* 23 */   private static final SimpleCommandExceptionType START_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.jfr.start.failed"));
/* 24 */   private static final DynamicCommandExceptionType DUMP_FAILED = new DynamicCommandExceptionType(message -> Component.translatableEscape("commands.jfr.dump.failed", new Object[] { message }));
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/* 30 */     dispatcher.register(
/* 31 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("jfr")
/* 32 */         .requires(Commands.hasPermission(Commands.LEVEL_OWNERS)))
/* 33 */         .then(Commands.literal("start").executes(c -> startJfr((CommandSourceStack)c.getSource()))))
/* 34 */         .then(Commands.literal("stop").executes(c -> stopJfr((CommandSourceStack)c.getSource()))));
/*    */   }
/*    */ 
/*    */   
/*    */   private static int startJfr(CommandSourceStack source) throws CommandSyntaxException {
/* 39 */     Environment env = Environment.from(source.getServer());
/* 40 */     if (!JvmProfiler.INSTANCE.start(env)) {
/* 41 */       throw START_FAILED.create();
/*    */     }
/* 43 */     source.sendSuccess(() -> Component.translatable("commands.jfr.started"), false);
/* 44 */     return 1;
/*    */   }
/*    */   
/*    */   private static int stopJfr(CommandSourceStack source) throws CommandSyntaxException {
/*    */     try {
/* 49 */       Path savedRecording = Paths.get(".", new String[0]).relativize(JvmProfiler.INSTANCE.stop().normalize());
/* 50 */       Path clipboardPath = (!source.getServer().isPublished() || SharedConstants.IS_RUNNING_IN_IDE) ? savedRecording.toAbsolutePath() : savedRecording;
/*    */ 
/*    */       
/* 53 */       MutableComponent mutableComponent = Component.literal(savedRecording.toString()).withStyle(ChatFormatting.UNDERLINE).withStyle(style -> style.withClickEvent(new ClickEvent.CopyToClipboard(clipboardPath.toString()))
/* 54 */           .withHoverEvent(new HoverEvent.ShowText(Component.translatable("chat.copy.click"))));
/*    */       
/* 56 */       source.sendSuccess(() -> Component.translatable("commands.jfr.stopped", new Object[] { fileText }), false);
/* 57 */       return 1;
/* 58 */     } catch (Throwable t) {
/* 59 */       throw DUMP_FAILED.create(t.getMessage());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\JfrCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */