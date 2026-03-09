/*    */ package net.minecraft.commands.synchronization.brigadier;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.arguments.DoubleArgumentType;
/*    */ import net.minecraft.commands.CommandBuildContext;
/*    */ import net.minecraft.commands.synchronization.ArgumentTypeInfo;
/*    */ import net.minecraft.commands.synchronization.ArgumentUtils;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ 
/*    */ public class DoubleArgumentInfo
/*    */   extends Object
/*    */   implements ArgumentTypeInfo<DoubleArgumentType, DoubleArgumentInfo.Template> {
/*    */   public final class Template extends Object implements ArgumentTypeInfo.Template<DoubleArgumentType> {
/*    */     private final double min;
/*    */     private final double max;
/*    */     
/*    */     private Template(double min, double max) {
/* 19 */       this.min = min;
/* 20 */       this.max = max;
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 25 */     public DoubleArgumentType instantiate(CommandBuildContext context) { return DoubleArgumentType.doubleArg(this.min, this.max); }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 30 */     public ArgumentTypeInfo<DoubleArgumentType, ?> type() { return DoubleArgumentInfo.this; }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void serializeToNetwork(Template template, FriendlyByteBuf out) {
/* 36 */     boolean hasMin = (template.min != -1.7976931348623157E308D);
/* 37 */     boolean hasMax = (template.max != Double.MAX_VALUE);
/* 38 */     out.writeByte(ArgumentUtils.createNumberFlags(hasMin, hasMax));
/* 39 */     if (hasMin) {
/* 40 */       out.writeDouble(template.min);
/*    */     }
/* 42 */     if (hasMax) {
/* 43 */       out.writeDouble(template.max);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public Template deserializeFromNetwork(FriendlyByteBuf in) {
/* 49 */     byte flags = in.readByte();
/* 50 */     double min = ArgumentUtils.numberHasMin(flags) ? in.readDouble() : -1.7976931348623157E308D;
/* 51 */     double max = ArgumentUtils.numberHasMax(flags) ? in.readDouble() : Double.MAX_VALUE;
/* 52 */     return new Template(min, max);
/*    */   }
/*    */ 
/*    */   
/*    */   public void serializeToJson(Template template, JsonObject out) {
/* 57 */     if (template.min != -1.7976931348623157E308D) {
/* 58 */       out.addProperty("min", Double.valueOf(template.min));
/*    */     }
/* 60 */     if (template.max != Double.MAX_VALUE) {
/* 61 */       out.addProperty("max", Double.valueOf(template.max));
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 67 */   public Template unpack(DoubleArgumentType argument) { return new Template(argument.getMinimum(), argument.getMaximum()); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\synchronization\brigadier\DoubleArgumentInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */