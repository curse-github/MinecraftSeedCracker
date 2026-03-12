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
/*     */ public class Info
/*     */   extends Object
/*     */   implements ArgumentTypeInfo<TimeArgument, TimeArgument.Info.Template>
/*     */ {
/*     */   public final class Template
/*     */     extends Object
/*     */     implements ArgumentTypeInfo.Template<TimeArgument>
/*     */   {
/*     */     private final int min;
/*     */     
/*  92 */     private Template(int min) { this.min = min; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  97 */     public TimeArgument instantiate(CommandBuildContext context) { return TimeArgument.time(this.min); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 102 */     public ArgumentTypeInfo<TimeArgument, ?> type() { return TimeArgument.Info.this; }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 108 */   public void serializeToNetwork(Template template, FriendlyByteBuf out) { out.writeInt(template.min); }
/*     */ 
/*     */ 
/*     */   
/*     */   public Template deserializeFromNetwork(FriendlyByteBuf in) {
/* 113 */     int min = in.readInt();
/* 114 */     return new Template(min);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 119 */   public void serializeToJson(Template template, JsonObject out) { out.addProperty("min", Integer.valueOf(template.min)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 124 */   public Template unpack(TimeArgument argument) { return new Template(argument.minimum); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\TimeArgument$Info.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */