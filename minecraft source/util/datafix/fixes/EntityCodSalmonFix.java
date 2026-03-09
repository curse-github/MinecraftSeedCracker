/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class EntityCodSalmonFix
/*    */   extends SimplestEntityRenameFix {
/*  9 */   public static final Map<String, String> RENAMED_IDS = ImmutableMap.builder()
/* 10 */     .put("minecraft:salmon_mob", "minecraft:salmon")
/* 11 */     .put("minecraft:cod_mob", "minecraft:cod")
/* 12 */     .build();
/*    */   
/* 14 */   public static final Map<String, String> RENAMED_EGG_IDS = ImmutableMap.builder()
/* 15 */     .put("minecraft:salmon_mob_spawn_egg", "minecraft:salmon_spawn_egg")
/* 16 */     .put("minecraft:cod_mob_spawn_egg", "minecraft:cod_spawn_egg")
/* 17 */     .build();
/*    */ 
/*    */   
/* 20 */   public EntityCodSalmonFix(Schema schema, boolean changesType) { super("EntityCodSalmonFix", schema, changesType); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 25 */   protected String rename(String name) { return (String)RENAMED_IDS.getOrDefault(name, name); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\EntityCodSalmonFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */