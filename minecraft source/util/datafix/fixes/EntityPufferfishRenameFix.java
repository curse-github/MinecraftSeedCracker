/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public class EntityPufferfishRenameFix
/*    */   extends SimplestEntityRenameFix {
/* 10 */   public static final Map<String, String> RENAMED_IDS = ImmutableMap.builder()
/* 11 */     .put("minecraft:puffer_fish_spawn_egg", "minecraft:pufferfish_spawn_egg")
/* 12 */     .build();
/*    */ 
/*    */   
/* 15 */   public EntityPufferfishRenameFix(Schema outputSchema, boolean changesType) { super("EntityPufferfishRenameFix", outputSchema, changesType); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 20 */   protected String rename(String name) { return Objects.equals("minecraft:puffer_fish", name) ? "minecraft:pufferfish" : name; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\EntityPufferfishRenameFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */