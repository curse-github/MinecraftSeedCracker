/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ 
/*    */ public abstract class DataComponentRemainderFix
/*    */   extends DataFix
/*    */ {
/*    */   private final String name;
/*    */   private final String componentId;
/*    */   private final String newComponentId;
/*    */   
/* 19 */   public DataComponentRemainderFix(Schema outputSchema, String name, String componentId) { this(outputSchema, name, componentId, componentId); }
/*    */ 
/*    */   
/*    */   public DataComponentRemainderFix(Schema outputSchema, String name, String componentId, String newComponentId) {
/* 23 */     super(outputSchema, false);
/* 24 */     this.name = name;
/* 25 */     this.componentId = componentId;
/* 26 */     this.newComponentId = newComponentId;
/*    */   }
/*    */ 
/*    */   
/*    */   public final TypeRewriteRule makeRule() {
/* 31 */     Type<?> dataComponentsType = getInputSchema().getType(References.DATA_COMPONENTS);
/* 32 */     return fixTypeEverywhereTyped(this.name, dataComponentsType, components -> 
/* 33 */         components.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */   
/*    */   protected abstract <T> Dynamic<T> fixComponent(Dynamic<T> paramDynamic);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\DataComponentRemainderFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */