/*    */ package net.minecraft.world.item.component;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.phys.EntityHitResult;
/*    */ 
/*    */ public final class PiercingWeapon extends Record {
/*    */   private final boolean dealsKnockback;
/*    */   private final boolean dismounts;
/*    */   private final Optional<Holder<SoundEvent>> sound;
/*    */   private final Optional<Holder<SoundEvent>> hitSound;
/*    */   
/* 25 */   public PiercingWeapon(boolean dealsKnockback, boolean dismounts, Optional<Holder<SoundEvent>> sound, Optional<Holder<SoundEvent>> hitSound) { this.dealsKnockback = dealsKnockback; this.dismounts = dismounts; this.sound = sound; this.hitSound = hitSound; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/PiercingWeapon;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #25	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 25 */     //   0	7	0	this	Lnet/minecraft/world/item/component/PiercingWeapon; } public boolean dealsKnockback() { return this.dealsKnockback; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/PiercingWeapon;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #25	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/PiercingWeapon; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/PiercingWeapon;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #25	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/component/PiercingWeapon;
/* 25 */     //   0	8	1	o	Ljava/lang/Object; } public boolean dismounts() { return this.dismounts; } public Optional<Holder<SoundEvent>> sound() { return this.sound; } public Optional<Holder<SoundEvent>> hitSound() { return this.hitSound; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 31 */   public static final Codec<PiercingWeapon> CODEC = RecordCodecBuilder.create(i -> i.group(Codec.BOOL
/* 32 */         .optionalFieldOf("deals_knockback", Boolean.valueOf(true)).forGetter(PiercingWeapon::dealsKnockback), Codec.BOOL
/* 33 */         .optionalFieldOf("dismounts", Boolean.valueOf(false)).forGetter(PiercingWeapon::dismounts), SoundEvent.CODEC
/* 34 */         .optionalFieldOf("sound").forGetter(PiercingWeapon::sound), SoundEvent.CODEC
/* 35 */         .optionalFieldOf("hit_sound").forGetter(PiercingWeapon::hitSound))
/*    */       
/* 37 */       .apply(i, PiercingWeapon::new));
/* 38 */   public static final StreamCodec<RegistryFriendlyByteBuf, PiercingWeapon> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.BOOL, PiercingWeapon::dealsKnockback, ByteBufCodecs.BOOL, PiercingWeapon::dismounts, SoundEvent.STREAM_CODEC
/*    */ 
/*    */       
/* 41 */       .apply(ByteBufCodecs::optional), PiercingWeapon::sound, SoundEvent.STREAM_CODEC
/* 42 */       .apply(ByteBufCodecs::optional), PiercingWeapon::hitSound, PiercingWeapon::new);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 47 */   public void makeSound(Entity causer) { this.sound.ifPresent(s -> causer.level().playSound(causer, causer.getX(), causer.getY(), causer.getZ(), s, causer.getSoundSource(), 1.0F, 1.0F)); }
/*    */ 
/*    */ 
/*    */   
/* 51 */   public void makeHitSound(Entity causer) { this.hitSound.ifPresent(s -> causer.level().playSound(null, causer.getX(), causer.getY(), causer.getZ(), s, causer.getSoundSource(), 1.0F, 1.0F)); }
/*    */ 
/*    */   
/*    */   public static boolean canHitEntity(Entity jabber, Entity target) {
/* 55 */     if (target.isInvulnerable() || !target.isAlive()) {
/* 56 */       return false;
/*    */     }
/*    */     
/* 59 */     if (target instanceof net.minecraft.world.entity.Interaction) {
/* 60 */       return true;
/*    */     }
/* 62 */     if (!target.canBeHitByProjectile()) {
/* 63 */       return false;
/*    */     }
/* 65 */     if (target instanceof Player) { Player targetPlayer = (Player)target; if (jabber instanceof Player) { Player jabbingPlayer = (Player)jabber; if (!jabbingPlayer.canHarmPlayer(targetPlayer))
/* 66 */           return false;  }
/*    */        }
/* 68 */      return !jabber.isPassengerOfSameVehicle(target);
/*    */   }
/*    */   
/*    */   public void attack(LivingEntity attacker, EquipmentSlot hand) {
/* 72 */     float damage = (float)attacker.getAttributeValue(Attributes.ATTACK_DAMAGE);
/*    */     
/* 74 */     AttackRange attackRange = attacker.entityAttackRange();
/*    */     
/* 76 */     boolean hitSomething = false;
/*    */     
/* 78 */     for (EntityHitResult hitResult : (Collection)ProjectileUtil.getHitEntitiesAlong(attacker, attackRange, e1 -> canHitEntity(attacker, e1), ClipContext.Block.COLLIDER).map(a -> List.of(), e -> e)) {
/* 79 */       hitSomething |= attacker.stabAttack(hand, hitResult.getEntity(), damage, true, this.dealsKnockback, this.dismounts);
/*    */     }
/*    */     
/* 82 */     attacker.onAttack();
/* 83 */     attacker.lungeForwardMaybe();
/* 84 */     if (hitSomething) {
/* 85 */       makeHitSound(attacker);
/*    */     }
/* 87 */     makeSound(attacker);
/*    */     
/* 89 */     attacker.swing(InteractionHand.MAIN_HAND, false);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\component\PiercingWeapon.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */