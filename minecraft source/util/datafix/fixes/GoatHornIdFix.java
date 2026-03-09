/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class GoatHornIdFix extends ItemStackTagRemainderFix {
/*  7 */   private static final String[] INSTRUMENTS = { "minecraft:ponder_goat_horn", "minecraft:sing_goat_horn", "minecraft:seek_goat_horn", "minecraft:feel_goat_horn", "minecraft:admire_goat_horn", "minecraft:call_goat_horn", "minecraft:yearn_goat_horn", "minecraft:dream_goat_horn" };
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 19 */   public GoatHornIdFix(Schema outputSchema) { super(outputSchema, "GoatHornIdFix", id -> id.equals("minecraft:goat_horn")); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected <T> Dynamic<T> fixItemStackTag(Dynamic<T> tag) {
/* 24 */     int soundVariant = tag.get("SoundVariant").asInt(0);
/* 25 */     String soundId = INSTRUMENTS[(soundVariant >= 0 && soundVariant < INSTRUMENTS.length) ? soundVariant : 0];
/* 26 */     return tag.remove("SoundVariant").set("instrument", tag.createString(soundId));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\GoatHornIdFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */