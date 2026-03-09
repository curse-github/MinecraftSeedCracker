/*     */ package net.minecraft.commands.arguments;
/*     */ 
/*     */ import com.google.gson.JsonObject;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.synchronization.ArgumentTypeInfo;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Info<T>
/*     */   extends Object
/*     */   implements ArgumentTypeInfo<ResourceOrTagArgument<T>, ResourceOrTagArgument.Info<T>.Template>
/*     */ {
/*     */   public final class Template
/*     */     extends Object
/*     */     implements ArgumentTypeInfo.Template<ResourceOrTagArgument<T>>
/*     */   {
/*     */     private final ResourceKey<? extends Registry<T>> registryKey;
/*     */     
/* 157 */     private Template(ResourceKey<? extends Registry<T>> registryKey) { this.registryKey = registryKey; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 162 */     public ResourceOrTagArgument<T> instantiate(CommandBuildContext context) { return new ResourceOrTagArgument(context, this.registryKey); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 167 */     public ArgumentTypeInfo<ResourceOrTagArgument<T>, ?> type() { return ResourceOrTagArgument.Info.this; }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 173 */   public void serializeToNetwork(Template template, FriendlyByteBuf out) { out.writeResourceKey(template.registryKey); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 178 */   public Template deserializeFromNetwork(FriendlyByteBuf in) { return new Template(in.readRegistryKey()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 183 */   public void serializeToJson(Template template, JsonObject out) { out.addProperty("registry", template.registryKey.identifier().toString()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 188 */   public Template unpack(ResourceOrTagArgument<T> argument) { return new Template(argument.registryKey); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\ResourceOrTagArgument$Info.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */