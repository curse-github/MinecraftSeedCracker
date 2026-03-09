/*     */ package net.minecraft.advancements.criterion;
/*     */ import com.mojang.datafixers.util.Function7;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.raid.Raid;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.level.block.entity.BannerPattern;
/*     */ 
/*     */ public final class EntityEquipmentPredicate extends Record {
/*     */   private final Optional<ItemPredicate> head;
/*     */   private final Optional<ItemPredicate> chest;
/*     */   private final Optional<ItemPredicate> legs;
/*     */   
/*  21 */   public EntityEquipmentPredicate(Optional<ItemPredicate> head, Optional<ItemPredicate> chest, Optional<ItemPredicate> legs, Optional<ItemPredicate> feet, Optional<ItemPredicate> body, Optional<ItemPredicate> mainhand, Optional<ItemPredicate> offhand) { this.head = head; this.chest = chest; this.legs = legs; this.feet = feet; this.body = body; this.mainhand = mainhand; this.offhand = offhand; } private final Optional<ItemPredicate> feet; private final Optional<ItemPredicate> body; private final Optional<ItemPredicate> mainhand; private final Optional<ItemPredicate> offhand; public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/EntityEquipmentPredicate;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #21	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/EntityEquipmentPredicate; } public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/EntityEquipmentPredicate;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #21	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/EntityEquipmentPredicate; } public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/EntityEquipmentPredicate;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #21	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/EntityEquipmentPredicate;
/*  21 */     //   0	8	1	o	Ljava/lang/Object; } public Optional<ItemPredicate> head() { return this.head; } public Optional<ItemPredicate> chest() { return this.chest; } public Optional<ItemPredicate> legs() { return this.legs; } public Optional<ItemPredicate> feet() { return this.feet; } public Optional<ItemPredicate> body() { return this.body; } public Optional<ItemPredicate> mainhand() { return this.mainhand; } public Optional<ItemPredicate> offhand() { return this.offhand; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  30 */   public static final Codec<EntityEquipmentPredicate> CODEC = RecordCodecBuilder.create(i -> i.group(ItemPredicate.CODEC
/*  31 */         .optionalFieldOf("head").forGetter(EntityEquipmentPredicate::head), ItemPredicate.CODEC
/*  32 */         .optionalFieldOf("chest").forGetter(EntityEquipmentPredicate::chest), ItemPredicate.CODEC
/*  33 */         .optionalFieldOf("legs").forGetter(EntityEquipmentPredicate::legs), ItemPredicate.CODEC
/*  34 */         .optionalFieldOf("feet").forGetter(EntityEquipmentPredicate::feet), ItemPredicate.CODEC
/*  35 */         .optionalFieldOf("body").forGetter(EntityEquipmentPredicate::body), ItemPredicate.CODEC
/*  36 */         .optionalFieldOf("mainhand").forGetter(EntityEquipmentPredicate::mainhand), ItemPredicate.CODEC
/*  37 */         .optionalFieldOf("offhand").forGetter(EntityEquipmentPredicate::offhand))
/*  38 */       .apply(i, EntityEquipmentPredicate::new));
/*     */ 
/*     */   
/*  41 */   public static EntityEquipmentPredicate captainPredicate(HolderGetter<Item> items, HolderGetter<BannerPattern> patternGetter) { return Builder.equipment()
/*  42 */       .head(
/*  43 */         ItemPredicate.Builder.item()
/*  44 */         .of(items, new ItemLike[] { Items.WHITE_BANNER
/*  45 */           }).withComponents(
/*  46 */           DataComponentMatchers.Builder.components().exact(DataComponentExactPredicate.someOf(
/*  47 */               Raid.getOminousBannerInstance(patternGetter).getComponents(), new DataComponentType[] { DataComponents.BANNER_PATTERNS, DataComponents.ITEM_NAME
/*     */ 
/*     */               
/*  50 */               })).build()))
/*     */       
/*  52 */       .build(); }
/*     */   
/*     */   public boolean matches(Entity entity) {
/*     */     LivingEntity livingEntity;
/*  56 */     if (entity instanceof LivingEntity) { livingEntity = (LivingEntity)entity; }
/*  57 */     else { return false; }
/*     */ 
/*     */     
/*  60 */     if (this.head.isPresent() && !((ItemPredicate)this.head.get()).test(livingEntity.getItemBySlot(EquipmentSlot.HEAD))) {
/*  61 */       return false;
/*     */     }
/*  63 */     if (this.chest.isPresent() && !((ItemPredicate)this.chest.get()).test(livingEntity.getItemBySlot(EquipmentSlot.CHEST))) {
/*  64 */       return false;
/*     */     }
/*  66 */     if (this.legs.isPresent() && !((ItemPredicate)this.legs.get()).test(livingEntity.getItemBySlot(EquipmentSlot.LEGS))) {
/*  67 */       return false;
/*     */     }
/*  69 */     if (this.feet.isPresent() && !((ItemPredicate)this.feet.get()).test(livingEntity.getItemBySlot(EquipmentSlot.FEET))) {
/*  70 */       return false;
/*     */     }
/*  72 */     if (this.body.isPresent() && !((ItemPredicate)this.body.get()).test(livingEntity.getItemBySlot(EquipmentSlot.BODY))) {
/*  73 */       return false;
/*     */     }
/*  75 */     if (this.mainhand.isPresent() && !((ItemPredicate)this.mainhand.get()).test(livingEntity.getItemBySlot(EquipmentSlot.MAINHAND))) {
/*  76 */       return false;
/*     */     }
/*  78 */     if (this.offhand.isPresent() && !((ItemPredicate)this.offhand.get()).test(livingEntity.getItemBySlot(EquipmentSlot.OFFHAND))) {
/*  79 */       return false;
/*     */     }
/*     */     
/*  82 */     return true;
/*     */   }
/*     */   
/*     */   public static class Builder {
/*  86 */     private Optional<ItemPredicate> head = Optional.empty();
/*  87 */     private Optional<ItemPredicate> chest = Optional.empty();
/*  88 */     private Optional<ItemPredicate> legs = Optional.empty();
/*  89 */     private Optional<ItemPredicate> feet = Optional.empty();
/*  90 */     private Optional<ItemPredicate> body = Optional.empty();
/*  91 */     private Optional<ItemPredicate> mainhand = Optional.empty();
/*  92 */     private Optional<ItemPredicate> offhand = Optional.empty();
/*     */ 
/*     */     
/*  95 */     public static Builder equipment() { return new Builder(); }
/*     */ 
/*     */     
/*     */     public Builder head(ItemPredicate.Builder head) {
/*  99 */       this.head = Optional.of(head.build());
/* 100 */       return this;
/*     */     }
/*     */     
/*     */     public Builder chest(ItemPredicate.Builder chest) {
/* 104 */       this.chest = Optional.of(chest.build());
/* 105 */       return this;
/*     */     }
/*     */     
/*     */     public Builder legs(ItemPredicate.Builder legs) {
/* 109 */       this.legs = Optional.of(legs.build());
/* 110 */       return this;
/*     */     }
/*     */     
/*     */     public Builder feet(ItemPredicate.Builder feet) {
/* 114 */       this.feet = Optional.of(feet.build());
/* 115 */       return this;
/*     */     }
/*     */     
/*     */     public Builder body(ItemPredicate.Builder body) {
/* 119 */       this.body = Optional.of(body.build());
/* 120 */       return this;
/*     */     }
/*     */     
/*     */     public Builder mainhand(ItemPredicate.Builder mainhand) {
/* 124 */       this.mainhand = Optional.of(mainhand.build());
/* 125 */       return this;
/*     */     }
/*     */     
/*     */     public Builder offhand(ItemPredicate.Builder offhand) {
/* 129 */       this.offhand = Optional.of(offhand.build());
/* 130 */       return this;
/*     */     }
/*     */ 
/*     */     
/* 134 */     public EntityEquipmentPredicate build() { return new EntityEquipmentPredicate(this.head, this.chest, this.legs, this.feet, this.body, this.mainhand, this.offhand); }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\EntityEquipmentPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */