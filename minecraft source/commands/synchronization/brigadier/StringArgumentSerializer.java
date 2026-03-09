/*    */ package net.minecraft.commands.synchronization.brigadier;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.arguments.StringArgumentType;
/*    */ import net.minecraft.commands.CommandBuildContext;
/*    */ import net.minecraft.commands.synchronization.ArgumentTypeInfo;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ 
/*    */ public class StringArgumentSerializer extends Object implements ArgumentTypeInfo<StringArgumentType, StringArgumentSerializer.Template> {
/*    */   public final class Template extends Object implements ArgumentTypeInfo.Template<StringArgumentType> {
/*    */     private final StringArgumentType.StringType type;
/*    */     
/* 14 */     public Template(StringArgumentType.StringType type) { this.type = type; }
/*    */ 
/*    */ 
/*    */     
/*    */     public StringArgumentType instantiate(CommandBuildContext context) {
/* 19 */       switch (StringArgumentSerializer.null.$SwitchMap$com$mojang$brigadier$arguments$StringArgumentType$StringType[this.type.ordinal()]) { default: throw new MatchException(null, null);case 1: case 2: case 3: break; }  return 
/*    */ 
/*    */         
/* 22 */         StringArgumentType.greedyString();
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 28 */     public ArgumentTypeInfo<StringArgumentType, ?> type() { return StringArgumentSerializer.this; }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 34 */   public void serializeToNetwork(Template template, FriendlyByteBuf out) { out.writeEnum(template.type); }
/*    */ 
/*    */ 
/*    */   
/*    */   public Template deserializeFromNetwork(FriendlyByteBuf in) {
/* 39 */     StringArgumentType.StringType type = (StringArgumentType.StringType)in.readEnum(StringArgumentType.StringType.class);
/* 40 */     return new Template(type);
/*    */   }
/*    */ 
/*    */   
/*    */   public void serializeToJson(Template template, JsonObject out) {
/* 45 */     switch (template.type) { default: throw new MatchException(null, null);case SINGLE_WORD: case QUOTABLE_PHRASE: case GREEDY_PHRASE: break; }  out.addProperty("type", 
/*    */ 
/*    */         
/* 48 */         "greedy");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 54 */   public Template unpack(StringArgumentType argument) { return new Template(argument.getType()); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\synchronization\brigadier\StringArgumentSerializer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */