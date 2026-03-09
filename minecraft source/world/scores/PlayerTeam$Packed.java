/*     */ package net.minecraft.world.scores;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function11;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
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
/*     */ public final class Packed
/*     */   extends Record
/*     */ {
/*     */   private final String name;
/*     */   private final Optional<Component> displayName;
/*     */   private final Optional<ChatFormatting> color;
/*     */   private final boolean allowFriendlyFire;
/*     */   private final boolean seeFriendlyInvisibles;
/*     */   private final Component memberNamePrefix;
/*     */   private final Component memberNameSuffix;
/*     */   private final Team.Visibility nameTagVisibility;
/*     */   private final Team.Visibility deathMessageVisibility;
/*     */   private final Team.CollisionRule collisionRule;
/*     */   private final List<String> players;
/*     */   
/*     */   public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/scores/PlayerTeam$Packed;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #217	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/scores/PlayerTeam$Packed; }
/*     */   
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/scores/PlayerTeam$Packed;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #217	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/scores/PlayerTeam$Packed; }
/*     */   
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/scores/PlayerTeam$Packed;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #217	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/scores/PlayerTeam$Packed;
/*     */     //   0	8	1	o	Ljava/lang/Object; }
/*     */   
/* 217 */   public Packed(String name, Optional<Component> displayName, Optional<ChatFormatting> color, boolean allowFriendlyFire, boolean seeFriendlyInvisibles, Component memberNamePrefix, Component memberNameSuffix, Team.Visibility nameTagVisibility, Team.Visibility deathMessageVisibility, Team.CollisionRule collisionRule, List<String> players) { this.name = name; this.displayName = displayName; this.color = color; this.allowFriendlyFire = allowFriendlyFire; this.seeFriendlyInvisibles = seeFriendlyInvisibles; this.memberNamePrefix = memberNamePrefix; this.memberNameSuffix = memberNameSuffix; this.nameTagVisibility = nameTagVisibility; this.deathMessageVisibility = deathMessageVisibility; this.collisionRule = collisionRule; this.players = players; } public String name() { return this.name; } public Optional<Component> displayName() { return this.displayName; } public Optional<ChatFormatting> color() { return this.color; } public boolean allowFriendlyFire() { return this.allowFriendlyFire; } public boolean seeFriendlyInvisibles() { return this.seeFriendlyInvisibles; } public Component memberNamePrefix() { return this.memberNamePrefix; } public Component memberNameSuffix() { return this.memberNameSuffix; } public Team.Visibility nameTagVisibility() { return this.nameTagVisibility; } public Team.Visibility deathMessageVisibility() { return this.deathMessageVisibility; } public Team.CollisionRule collisionRule() { return this.collisionRule; } public List<String> players() { return this.players; }
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
/* 230 */   public static final Codec<Packed> CODEC = RecordCodecBuilder.create(i -> i.group(Codec.STRING
/* 231 */         .fieldOf("Name").forGetter(Packed::name), ComponentSerialization.CODEC
/* 232 */         .optionalFieldOf("DisplayName").forGetter(Packed::displayName), ChatFormatting.COLOR_CODEC
/* 233 */         .optionalFieldOf("TeamColor").forGetter(Packed::color), Codec.BOOL
/* 234 */         .optionalFieldOf("AllowFriendlyFire", Boolean.valueOf(true)).forGetter(Packed::allowFriendlyFire), Codec.BOOL
/* 235 */         .optionalFieldOf("SeeFriendlyInvisibles", Boolean.valueOf(true)).forGetter(Packed::seeFriendlyInvisibles), ComponentSerialization.CODEC
/* 236 */         .optionalFieldOf("MemberNamePrefix", CommonComponents.EMPTY).forGetter(Packed::memberNamePrefix), ComponentSerialization.CODEC
/* 237 */         .optionalFieldOf("MemberNameSuffix", CommonComponents.EMPTY).forGetter(Packed::memberNameSuffix), Team.Visibility.CODEC
/* 238 */         .optionalFieldOf("NameTagVisibility", Team.Visibility.ALWAYS).forGetter(Packed::nameTagVisibility), Team.Visibility.CODEC
/* 239 */         .optionalFieldOf("DeathMessageVisibility", Team.Visibility.ALWAYS).forGetter(Packed::deathMessageVisibility), Team.CollisionRule.CODEC
/* 240 */         .optionalFieldOf("CollisionRule", Team.CollisionRule.ALWAYS).forGetter(Packed::collisionRule), Codec.STRING
/* 241 */         .listOf().optionalFieldOf("Players", List.of()).forGetter(Packed::players))
/* 242 */       .apply(i, Packed::new));
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\scores\PlayerTeam$Packed.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */