/*     */ package net.minecraft.core.component.predicates;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class AnyValueType
/*     */   extends DataComponentPredicate.TypeBase<AnyValue>
/*     */ {
/*     */   private final AnyValue predicate;
/*     */   
/*     */   public AnyValueType(AnyValue predicate) {
/* 110 */     super(MapCodec.unitCodec(predicate));
/* 111 */     this.predicate = predicate;
/*     */   }
/*     */ 
/*     */   
/* 115 */   public AnyValue predicate() { return this.predicate; }
/*     */ 
/*     */ 
/*     */   
/* 119 */   public DataComponentType<?> componentType() { return this.predicate.type(); }
/*     */ 
/*     */ 
/*     */   
/* 123 */   public static AnyValueType create(DataComponentType<?> componentType) { return new AnyValueType(new AnyValue(componentType)); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\component\predicates\DataComponentPredicate$AnyValueType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */