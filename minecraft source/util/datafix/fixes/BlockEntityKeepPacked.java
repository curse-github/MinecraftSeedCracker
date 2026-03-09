/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class BlockEntityKeepPacked
/*    */   extends NamedEntityFix {
/* 10 */   public BlockEntityKeepPacked(Schema schema, boolean changesType) { super(schema, changesType, "BlockEntityKeepPacked", References.BLOCK_ENTITY, "DUMMY"); }
/*    */ 
/*    */ 
/*    */   
/* 14 */   private static Dynamic<?> fixTag(Dynamic<?> tag) { return tag.set("keepPacked", tag.createBoolean(true)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 19 */   protected Typed<?> fix(Typed<?> entity) { return entity.update(DSL.remainderFinder(), BlockEntityKeepPacked::fixTag); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\BlockEntityKeepPacked.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */