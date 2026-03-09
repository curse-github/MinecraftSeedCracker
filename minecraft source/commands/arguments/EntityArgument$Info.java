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
/*     */ public class Info
/*     */   extends Object
/*     */   implements ArgumentTypeInfo<EntityArgument, EntityArgument.Info.Template>
/*     */ {
/*     */   private static final byte FLAG_SINGLE = 1;
/*     */   private static final byte FLAG_PLAYERS_ONLY = 2;
/*     */   
/*     */   public final class Template
/*     */     extends Object
/*     */     implements ArgumentTypeInfo.Template<EntityArgument>
/*     */   {
/*     */     private final boolean single;
/*     */     private final boolean playersOnly;
/*     */     
/*     */     private Template(boolean single, boolean playersOnly) {
/* 160 */       this.single = single;
/* 161 */       this.playersOnly = playersOnly;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 166 */     public EntityArgument instantiate(CommandBuildContext context) { return new EntityArgument(this.single, this.playersOnly); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 171 */     public ArgumentTypeInfo<EntityArgument, ?> type() { return EntityArgument.Info.this; }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void serializeToNetwork(Template template, FriendlyByteBuf out) {
/* 177 */     int flags = 0;
/* 178 */     if (template.single) {
/* 179 */       flags |= 0x1;
/*     */     }
/* 181 */     if (template.playersOnly) {
/* 182 */       flags |= 0x2;
/*     */     }
/* 184 */     out.writeByte(flags);
/*     */   }
/*     */ 
/*     */   
/*     */   public Template deserializeFromNetwork(FriendlyByteBuf in) {
/* 189 */     byte flags = in.readByte();
/* 190 */     return new Template(((flags & true) != 0), ((flags & 0x2) != 0));
/*     */   }
/*     */ 
/*     */   
/*     */   public void serializeToJson(Template template, JsonObject out) {
/* 195 */     out.addProperty("amount", template.single ? "single" : "multiple");
/* 196 */     out.addProperty("type", template.playersOnly ? "players" : "entities");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 201 */   public Template unpack(EntityArgument argument) { return new Template(argument.single, argument.playersOnly); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\EntityArgument$Info.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */