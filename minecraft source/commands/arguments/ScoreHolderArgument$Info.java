/*     */ package net.minecraft.commands.arguments;
/*     */ 
/*     */ import com.google.gson.JsonObject;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.synchronization.ArgumentTypeInfo;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Info
/*     */   extends Object
/*     */   implements ArgumentTypeInfo<ScoreHolderArgument, ScoreHolderArgument.Info.Template>
/*     */ {
/*     */   private static final byte FLAG_MULTIPLE = 1;
/*     */   
/*     */   public final class Template
/*     */     extends Object
/*     */     implements ArgumentTypeInfo.Template<ScoreHolderArgument>
/*     */   {
/*     */     private final boolean multiple;
/*     */     
/* 209 */     private Template(boolean multiple) { this.multiple = multiple; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 214 */     public ScoreHolderArgument instantiate(CommandBuildContext context) { return new ScoreHolderArgument(this.multiple); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 219 */     public ArgumentTypeInfo<ScoreHolderArgument, ?> type() { return ScoreHolderArgument.Info.this; }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void serializeToNetwork(Template template, FriendlyByteBuf out) {
/* 225 */     int flags = 0;
/* 226 */     if (template.multiple) {
/* 227 */       flags |= 0x1;
/*     */     }
/* 229 */     out.writeByte(flags);
/*     */   }
/*     */ 
/*     */   
/*     */   public Template deserializeFromNetwork(FriendlyByteBuf in) {
/* 234 */     byte flags = in.readByte();
/* 235 */     boolean multiple = ((flags & true) != 0);
/* 236 */     return new Template(multiple);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 241 */   public void serializeToJson(Template template, JsonObject out) { out.addProperty("amount", template.multiple ? "multiple" : "single"); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 246 */   public Template unpack(ScoreHolderArgument argument) { return new Template(argument.multiple); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\ScoreHolderArgument$Info.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */