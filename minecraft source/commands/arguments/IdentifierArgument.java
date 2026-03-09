/*    */ package net.minecraft.commands.arguments;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class IdentifierArgument
/*    */   extends Object implements ArgumentType<Identifier> {
/* 14 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "foo", "foo:bar", "012" });
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 20 */   public static IdentifierArgument id() { return new IdentifierArgument(); }
/*    */ 
/*    */ 
/*    */   
/* 24 */   public static Identifier getId(CommandContext<CommandSourceStack> context, String name) { return (Identifier)context.getArgument(name, Identifier.class); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 29 */   public Identifier parse(StringReader reader) throws CommandSyntaxException { return Identifier.read(reader); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 34 */   public Collection<String> getExamples() { return EXAMPLES; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\IdentifierArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */