/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public class EntityZombifiedPiglinRenameFix
/*    */   extends SimplestEntityRenameFix {
/* 10 */   public static final Map<String, String> RENAMED_IDS = ImmutableMap.builder()
/* 11 */     .put("minecraft:zombie_pigman_spawn_egg", "minecraft:zombified_piglin_spawn_egg")
/* 12 */     .build();
/*    */ 
/*    */   
/* 15 */   public EntityZombifiedPiglinRenameFix(Schema outputSchema) { super("EntityZombifiedPiglinRenameFix", outputSchema, true); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 20 */   protected String rename(String name) { return Objects.equals("minecraft:zombie_pigman", name) ? "minecraft:zombified_piglin" : name; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\EntityZombifiedPiglinRenameFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */