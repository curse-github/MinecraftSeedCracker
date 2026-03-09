/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.world.damagesource.DamageType;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Builder
/*    */ {
/* 53 */   private final ImmutableList.Builder<TagPredicate<DamageType>> tags = ImmutableList.builder();
/* 54 */   private Optional<EntityPredicate> directEntity = Optional.empty();
/* 55 */   private Optional<EntityPredicate> sourceEntity = Optional.empty();
/* 56 */   private Optional<Boolean> isDirect = Optional.empty();
/*    */ 
/*    */   
/* 59 */   public static Builder damageType() { return new Builder(); }
/*    */ 
/*    */   
/*    */   public Builder tag(TagPredicate<DamageType> tag) {
/* 63 */     this.tags.add(tag);
/* 64 */     return this;
/*    */   }
/*    */   
/*    */   public Builder direct(EntityPredicate.Builder directEntity) {
/* 68 */     this.directEntity = Optional.of(directEntity.build());
/* 69 */     return this;
/*    */   }
/*    */   
/*    */   public Builder source(EntityPredicate.Builder sourceEntity) {
/* 73 */     this.sourceEntity = Optional.of(sourceEntity.build());
/* 74 */     return this;
/*    */   }
/*    */   
/*    */   public Builder isDirect(boolean direct) {
/* 78 */     this.isDirect = Optional.of(Boolean.valueOf(direct));
/* 79 */     return this;
/*    */   }
/*    */ 
/*    */   
/* 83 */   public DamageSourcePredicate build() { return new DamageSourcePredicate(this.tags.build(), this.directEntity, this.sourceEntity, this.isDirect); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\DamageSourcePredicate$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */