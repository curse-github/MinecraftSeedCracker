/*    */ package net.minecraft.network.chat.contents.data;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public class DataSources {
/*  8 */   private static final ExtraCodecs.LateBoundIdMapper<String, MapCodec<? extends DataSource>> ID_MAPPER = new ExtraCodecs.LateBoundIdMapper();
/*    */   
/* 10 */   public static final MapCodec<DataSource> CODEC = ComponentSerialization.createLegacyComponentMatcher(ID_MAPPER, DataSource::codec, "source");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static  {
/* 17 */     ID_MAPPER.put("entity", EntityDataSource.MAP_CODEC);
/* 18 */     ID_MAPPER.put("block", BlockDataSource.MAP_CODEC);
/* 19 */     ID_MAPPER.put("storage", StorageDataSource.MAP_CODEC);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\contents\data\DataSources.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */