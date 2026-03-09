/*    */ package net.minecraft.commands.arguments;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.TagParser;
/*    */ 
/*    */ public class CompoundTagArgument
/*    */   extends Object implements ArgumentType<CompoundTag> {
/* 14 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "{}", "{foo=bar}" });
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 20 */   public static CompoundTagArgument compoundTag() { return new CompoundTagArgument(); }
/*    */ 
/*    */ 
/*    */   
/* 24 */   public static <S> CompoundTag getCompoundTag(CommandContext<S> context, String name) { return (CompoundTag)context.getArgument(name, CompoundTag.class); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 29 */   public CompoundTag parse(StringReader reader) throws CommandSyntaxException { return TagParser.parseCompoundAsArgument(reader); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 34 */   public Collection<String> getExamples() { return EXAMPLES; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\CompoundTagArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */