/*    */ package net.minecraft.commands.arguments;
/*    */ 
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import net.minecraft.nbt.NbtOps;
/*    */ import net.minecraft.nbt.SnbtGrammar;
/*    */ import net.minecraft.nbt.Tag;
/*    */ import net.minecraft.util.parsing.packrat.commands.CommandArgumentParser;
/*    */ import net.minecraft.util.parsing.packrat.commands.ParserBasedArgument;
/*    */ 
/*    */ public class NbtTagArgument
/*    */   extends ParserBasedArgument<Tag> {
/* 14 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "0", "0b", "0l", "0.0", "\"foo\"", "{foo=bar}", "[0]" });
/*    */   
/* 16 */   private static final CommandArgumentParser<Tag> TAG_PARSER = SnbtGrammar.createParser(NbtOps.INSTANCE);
/*    */ 
/*    */   
/* 19 */   private NbtTagArgument() { super(TAG_PARSER); }
/*    */ 
/*    */ 
/*    */   
/* 23 */   public static NbtTagArgument nbtTag() { return new NbtTagArgument(); }
/*    */ 
/*    */ 
/*    */   
/* 27 */   public static <S> Tag getNbtTag(CommandContext<S> context, String name) { return (Tag)context.getArgument(name, Tag.class); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 32 */   public Collection<String> getExamples() { return EXAMPLES; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\NbtTagArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */