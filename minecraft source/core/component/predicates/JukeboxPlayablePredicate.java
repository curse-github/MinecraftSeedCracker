/*    */ package net.minecraft.core.component.predicates;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.advancements.criterion.SingleComponentItemPredicate;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.RegistryCodecs;
/*    */ import net.minecraft.core.component.DataComponentType;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.item.JukeboxPlayable;
/*    */ import net.minecraft.world.item.JukeboxSong;
/*    */ 
/*    */ public final class JukeboxPlayablePredicate extends Record implements SingleComponentItemPredicate<JukeboxPlayable> {
/* 18 */   public JukeboxPlayablePredicate(Optional<HolderSet<JukeboxSong>> song) { this.song = song; } private final Optional<HolderSet<JukeboxSong>> song; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/component/predicates/JukeboxPlayablePredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 18 */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/JukeboxPlayablePredicate; } public Optional<HolderSet<JukeboxSong>> song() { return this.song; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/component/predicates/JukeboxPlayablePredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/JukeboxPlayablePredicate; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/component/predicates/JukeboxPlayablePredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/core/component/predicates/JukeboxPlayablePredicate;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 22 */   public static final Codec<JukeboxPlayablePredicate> CODEC = RecordCodecBuilder.create(i -> i.group(
/* 23 */         RegistryCodecs.homogeneousList(Registries.JUKEBOX_SONG).optionalFieldOf("song").forGetter(JukeboxPlayablePredicate::song))
/* 24 */       .apply(i, JukeboxPlayablePredicate::new));
/*    */ 
/*    */ 
/*    */   
/* 28 */   public DataComponentType<JukeboxPlayable> componentType() { return DataComponents.JUKEBOX_PLAYABLE; }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean matches(JukeboxPlayable value) {
/* 33 */     if (this.song.isPresent()) {
/* 34 */       boolean songIsPresent = false;
/* 35 */       for (Holder<JukeboxSong> maybeSong : (HolderSet)this.song.get()) {
/* 36 */         Optional<ResourceKey<JukeboxSong>> songId = maybeSong.unwrapKey();
/* 37 */         if (songId.isEmpty()) {
/*    */           continue;
/*    */         }
/*    */         
/* 41 */         if (songId.equals(value.song().key())) {
/* 42 */           songIsPresent = true;
/*    */           
/*    */           break;
/*    */         } 
/*    */       } 
/* 47 */       return songIsPresent;
/*    */     } 
/*    */     
/* 50 */     return true;
/*    */   }
/*    */ 
/*    */   
/* 54 */   public static JukeboxPlayablePredicate any() { return new JukeboxPlayablePredicate(Optional.empty()); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\component\predicates\JukeboxPlayablePredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */