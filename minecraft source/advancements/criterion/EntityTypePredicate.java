/*    */ package net.minecraft.advancements.criterion;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.tags.TagKey;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ 
/*    */ public final class EntityTypePredicate extends Record {
/*    */   private final HolderSet<EntityType<?>> types;
/*    */   
/* 11 */   public EntityTypePredicate(HolderSet<EntityType<?>> types) { this.types = types; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/EntityTypePredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 11 */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/EntityTypePredicate; } public HolderSet<EntityType<?>> types() { return this.types; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/EntityTypePredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/EntityTypePredicate; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/EntityTypePredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/EntityTypePredicate;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 12 */   public static final Codec<EntityTypePredicate> CODEC = RegistryCodecs.homogeneousList(Registries.ENTITY_TYPE).xmap(EntityTypePredicate::new, EntityTypePredicate::types);
/*    */ 
/*    */ 
/*    */   
/* 16 */   public static EntityTypePredicate of(HolderGetter<EntityType<?>> lookup, EntityType<?> type) { return new EntityTypePredicate(HolderSet.direct(new Holder[] { type.builtInRegistryHolder() })); }
/*    */ 
/*    */ 
/*    */   
/* 20 */   public static EntityTypePredicate of(HolderGetter<EntityType<?>> lookup, TagKey<EntityType<?>> type) { return new EntityTypePredicate(lookup.getOrThrow(type)); }
/*    */ 
/*    */ 
/*    */   
/* 24 */   public boolean matches(EntityType<?> type) { return type.is(this.types); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\EntityTypePredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */