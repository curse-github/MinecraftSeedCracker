/*    */ package net.minecraft.world.entity.ai.attributes;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public class Attribute {
/* 13 */   public static final Codec<Holder<Attribute>> CODEC = BuiltInRegistries.ATTRIBUTE.holderByNameCodec();
/* 14 */   public static final StreamCodec<RegistryFriendlyByteBuf, Holder<Attribute>> STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.ATTRIBUTE); private final double defaultValue; private boolean syncable;
/*    */   private final String descriptionId;
/*    */   private Sentiment sentiment;
/*    */   
/*    */   protected Attribute(String descriptionId, double defaultValue) {
/* 19 */     this.sentiment = Sentiment.POSITIVE;
/*    */ 
/*    */     
/* 22 */     this.defaultValue = defaultValue;
/* 23 */     this.descriptionId = descriptionId;
/*    */   }
/*    */ 
/*    */   
/* 27 */   public double getDefaultValue() { return this.defaultValue; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 32 */   public boolean isClientSyncable() { return this.syncable; }
/*    */ 
/*    */   
/*    */   public Attribute setSyncable(boolean syncable) {
/* 36 */     this.syncable = syncable;
/* 37 */     return this;
/*    */   }
/*    */   
/*    */   public Attribute setSentiment(Sentiment sentiment) {
/* 41 */     this.sentiment = sentiment;
/* 42 */     return this;
/*    */   }
/*    */ 
/*    */   
/* 46 */   public double sanitizeValue(double value) { return value; }
/*    */ 
/*    */ 
/*    */   
/* 50 */   public String getDescriptionId() { return this.descriptionId; }
/*    */ 
/*    */ 
/*    */   
/* 54 */   public ChatFormatting getStyle(boolean valueIncrease) { return this.sentiment.getStyle(valueIncrease); }
/*    */   
/*    */   public enum Sentiment
/*    */   {
/* 58 */     POSITIVE,
/* 59 */     NEUTRAL,
/* 60 */     NEGATIVE;
/*    */ 
/*    */     
/*    */     public ChatFormatting getStyle(boolean valueIncrease) {
/* 64 */       switch (ordinal()) { default: throw new MatchException(null, null);case 0: return 
/* 65 */             valueIncrease ? ChatFormatting.BLUE : ChatFormatting.RED;
/*    */         case 1: 
/* 67 */         case 2: break; }  return valueIncrease ? ChatFormatting.RED : ChatFormatting.BLUE;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\attributes\Attribute.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */