/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class JigsawRotationFix
/*    */   extends AbstractBlockPropertyFix {
/* 10 */   private static final Map<String, String> RENAMES = ImmutableMap.builder()
/* 11 */     .put("down", "down_south")
/* 12 */     .put("up", "up_north")
/* 13 */     .put("north", "north_up")
/* 14 */     .put("south", "south_up")
/* 15 */     .put("west", "west_up")
/* 16 */     .put("east", "east_up")
/* 17 */     .build();
/*    */ 
/*    */   
/* 20 */   public JigsawRotationFix(Schema outputSchema) { super(outputSchema, "jigsaw_rotation_fix"); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 25 */   protected boolean shouldFix(String blockId) { return blockId.equals("minecraft:jigsaw"); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected <T> Dynamic<T> fixProperties(String blockId, Dynamic<T> properties) {
/* 30 */     String facing = properties.get("facing").asString("north");
/* 31 */     return properties
/* 32 */       .remove("facing")
/* 33 */       .set("orientation", properties.createString((String)RENAMES.getOrDefault(facing, facing)));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\JigsawRotationFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */