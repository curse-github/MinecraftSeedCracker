/*    */ package net.minecraft.world.item.component;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public final class Weapon extends Record {
/*    */   private final int itemDamagePerAttack;
/*    */   private final float disableBlockingForSeconds;
/*    */   public static final float AXE_DISABLES_BLOCKING_FOR_SECONDS = 5.0F;
/*    */   
/* 10 */   public Weapon(int itemDamagePerAttack, float disableBlockingForSeconds) { this.itemDamagePerAttack = itemDamagePerAttack; this.disableBlockingForSeconds = disableBlockingForSeconds; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/Weapon;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/world/item/component/Weapon; } public int itemDamagePerAttack() { return this.itemDamagePerAttack; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/Weapon;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/Weapon; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/Weapon;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/component/Weapon;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public float disableBlockingForSeconds() { return this.disableBlockingForSeconds; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 16 */   public static final Codec<Weapon> CODEC = RecordCodecBuilder.create(i -> i.group(ExtraCodecs.NON_NEGATIVE_INT
/* 17 */         .optionalFieldOf("item_damage_per_attack", Integer.valueOf(1)).forGetter(Weapon::itemDamagePerAttack), ExtraCodecs.NON_NEGATIVE_FLOAT
/* 18 */         .optionalFieldOf("disable_blocking_for_seconds", Float.valueOf(0.0F)).forGetter(Weapon::disableBlockingForSeconds))
/* 19 */       .apply(i, Weapon::new));
/*    */   
/* 21 */   public static final StreamCodec<RegistryFriendlyByteBuf, Weapon> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, Weapon::itemDamagePerAttack, ByteBufCodecs.FLOAT, Weapon::disableBlockingForSeconds, Weapon::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 28 */   public Weapon(int damagePerAttack) { this(damagePerAttack, 0.0F); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\component\Weapon.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */