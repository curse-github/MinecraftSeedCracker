/*    */ package net.minecraft.commands.synchronization.brigadier;
/*    */ 
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.arguments.StringArgumentType;
/*    */ import net.minecraft.commands.CommandBuildContext;
/*    */ import net.minecraft.commands.synchronization.ArgumentTypeInfo;
/*    */ 
/*    */ public final class Template
/*    */   extends Object
/*    */   implements ArgumentTypeInfo.Template<StringArgumentType>
/*    */ {
/*    */   private final StringArgumentType.StringType type;
/*    */   
/* 14 */   public Template(StringArgumentType.StringType type) { this.type = type; }
/*    */ 
/*    */ 
/*    */   
/*    */   public StringArgumentType instantiate(CommandBuildContext context) {
/* 19 */     switch (StringArgumentSerializer.null.$SwitchMap$com$mojang$brigadier$arguments$StringArgumentType$StringType[this.type.ordinal()]) { default: throw new MatchException(null, null);case 1: case 2: case 3: break; }  return 
/*    */ 
/*    */       
/* 22 */       StringArgumentType.greedyString();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 28 */   public ArgumentTypeInfo<StringArgumentType, ?> type() { return StringArgumentSerializer.this; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\synchronization\brigadier\StringArgumentSerializer$Template.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */