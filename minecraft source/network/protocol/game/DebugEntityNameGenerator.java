/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.util.UUID;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DebugEntityNameGenerator
/*    */ {
/*    */   private static final String[] NAMES_FIRST_PART = { 
/* 17 */       "Slim", "Far", "River", "Silly", "Fat", "Thin", "Fish", "Bat", "Dark", "Oak", "Sly", "Bush", "Zen", "Bark", "Cry", "Slack", "Soup", "Grim", "Hook", "Dirt", "Mud", "Sad", "Hard", "Crook", "Sneak", "Stink", "Weird", "Fire", "Soot", "Soft", "Rough", "Cling", "Scar" };
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static final String[] NAMES_SECOND_PART = { 
/* 24 */       "Fox", "Tail", "Jaw", "Whisper", "Twig", "Root", "Finder", "Nose", "Brow", "Blade", "Fry", "Seek", "Wart", "Tooth", "Foot", "Leaf", "Stone", "Fall", "Face", "Tongue", "Voice", "Lip", "Mouth", "Snail", "Toe", "Ear", "Hair", "Beard", "Shirt", "Fist" };
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String getEntityName(Entity entity) {
/* 31 */     if (entity instanceof net.minecraft.world.entity.player.Player) {
/* 32 */       return entity.getPlainTextName();
/*    */     }
/* 34 */     Component customName = entity.getCustomName();
/* 35 */     if (customName != null) {
/* 36 */       return customName.getString();
/*    */     }
/* 38 */     return getEntityName(entity.getUUID());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String getEntityName(UUID uuid) {
/* 47 */     RandomSource random = getRandom(uuid);
/* 48 */     return getRandomString(random, NAMES_FIRST_PART) + getRandomString(random, NAMES_FIRST_PART);
/*    */   }
/*    */ 
/*    */   
/* 52 */   private static String getRandomString(RandomSource random, String[] names) { return (String)Util.getRandom(names, random); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 57 */   private static RandomSource getRandom(UUID uuid) { return RandomSource.create((uuid.hashCode() >> 2)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\DebugEntityNameGenerator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */