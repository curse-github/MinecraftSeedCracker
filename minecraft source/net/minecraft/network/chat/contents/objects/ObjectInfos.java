/*    */ package net.minecraft.network.chat.contents.objects;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public class ObjectInfos {
/*  8 */   private static final ExtraCodecs.LateBoundIdMapper<String, MapCodec<? extends ObjectInfo>> ID_MAPPER = new ExtraCodecs.LateBoundIdMapper();
/*    */   
/* 10 */   public static final MapCodec<ObjectInfo> CODEC = ComponentSerialization.createLegacyComponentMatcher(ID_MAPPER, ObjectInfo::codec, "object");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static  {
/* 17 */     ID_MAPPER.put("atlas", AtlasSprite.MAP_CODEC);
/* 18 */     ID_MAPPER.put("player", PlayerSprite.MAP_CODEC);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\contents\objects\ObjectInfos.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */