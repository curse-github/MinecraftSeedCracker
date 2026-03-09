/*    */ package net.minecraft.commands.arguments;
/*    */ 
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import net.minecraft.commands.CommandBuildContext;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.nbt.NbtOps;
/*    */ import net.minecraft.nbt.SnbtGrammar;
/*    */ import net.minecraft.nbt.Tag;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.Style;
/*    */ import net.minecraft.util.parsing.packrat.commands.CommandArgumentParser;
/*    */ import net.minecraft.util.parsing.packrat.commands.ParserBasedArgument;
/*    */ 
/*    */ public class StyleArgument extends ParserBasedArgument<Style> {
/* 21 */   private static final Collection<String> EXAMPLES = List.of("{bold: true}", "{color: 'red'}", "{}");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   public static final DynamicCommandExceptionType ERROR_INVALID_STYLE = new DynamicCommandExceptionType(message -> Component.translatableEscape("argument.style.invalid", new Object[] { message }));
/*    */   
/* 29 */   private static final DynamicOps<Tag> OPS = NbtOps.INSTANCE;
/* 30 */   private static final CommandArgumentParser<Tag> TAG_PARSER = SnbtGrammar.createParser(OPS);
/*    */   
/*    */   private StyleArgument(HolderLookup.Provider registries) {
/* 33 */     super(TAG_PARSER.withCodec(registries
/* 34 */           .createSerializationContext(OPS), TAG_PARSER, Style.Serializer.CODEC, ERROR_INVALID_STYLE));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 42 */   public static Style getStyle(CommandContext<CommandSourceStack> context, String name) { return (Style)context.getArgument(name, Style.class); }
/*    */ 
/*    */ 
/*    */   
/* 46 */   public static StyleArgument style(CommandBuildContext context) { return new StyleArgument(context); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 51 */   public Collection<String> getExamples() { return EXAMPLES; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\StyleArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */