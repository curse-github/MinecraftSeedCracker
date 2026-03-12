/*     */ package net.minecraft.network.protocol.game;
/*     */ 
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.world.scores.PlayerTeam;
/*     */ import net.minecraft.world.scores.Team;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Parameters
/*     */ {
/*     */   private final Component displayName;
/*     */   private final Component playerPrefix;
/*     */   private final Component playerSuffix;
/*     */   private final Team.Visibility nametagVisibility;
/*     */   private final Team.CollisionRule collisionRule;
/*     */   private final ChatFormatting color;
/*     */   private final int options;
/*     */   
/*     */   public Parameters(PlayerTeam team) {
/* 162 */     this.displayName = team.getDisplayName();
/* 163 */     this.options = team.packOptions();
/* 164 */     this.nametagVisibility = team.getNameTagVisibility();
/* 165 */     this.collisionRule = team.getCollisionRule();
/* 166 */     this.color = team.getColor();
/* 167 */     this.playerPrefix = team.getPlayerPrefix();
/* 168 */     this.playerSuffix = team.getPlayerSuffix();
/*     */   }
/*     */   
/*     */   public Parameters(RegistryFriendlyByteBuf input) {
/* 172 */     this.displayName = (Component)ComponentSerialization.TRUSTED_STREAM_CODEC.decode(input);
/* 173 */     this.options = input.readByte();
/* 174 */     this.nametagVisibility = (Team.Visibility)Team.Visibility.STREAM_CODEC.decode(input);
/* 175 */     this.collisionRule = (Team.CollisionRule)Team.CollisionRule.STREAM_CODEC.decode(input);
/* 176 */     this.color = (ChatFormatting)input.readEnum(ChatFormatting.class);
/* 177 */     this.playerPrefix = (Component)ComponentSerialization.TRUSTED_STREAM_CODEC.decode(input);
/* 178 */     this.playerSuffix = (Component)ComponentSerialization.TRUSTED_STREAM_CODEC.decode(input);
/*     */   }
/*     */ 
/*     */   
/* 182 */   public Component getDisplayName() { return this.displayName; }
/*     */ 
/*     */ 
/*     */   
/* 186 */   public int getOptions() { return this.options; }
/*     */ 
/*     */ 
/*     */   
/* 190 */   public ChatFormatting getColor() { return this.color; }
/*     */ 
/*     */ 
/*     */   
/* 194 */   public Team.Visibility getNametagVisibility() { return this.nametagVisibility; }
/*     */ 
/*     */ 
/*     */   
/* 198 */   public Team.CollisionRule getCollisionRule() { return this.collisionRule; }
/*     */ 
/*     */ 
/*     */   
/* 202 */   public Component getPlayerPrefix() { return this.playerPrefix; }
/*     */ 
/*     */ 
/*     */   
/* 206 */   public Component getPlayerSuffix() { return this.playerSuffix; }
/*     */ 
/*     */   
/*     */   public void write(RegistryFriendlyByteBuf output) {
/* 210 */     ComponentSerialization.TRUSTED_STREAM_CODEC.encode(output, this.displayName);
/* 211 */     output.writeByte(this.options);
/* 212 */     Team.Visibility.STREAM_CODEC.encode(output, this.nametagVisibility);
/* 213 */     Team.CollisionRule.STREAM_CODEC.encode(output, this.collisionRule);
/* 214 */     output.writeEnum(this.color);
/* 215 */     ComponentSerialization.TRUSTED_STREAM_CODEC.encode(output, this.playerPrefix);
/* 216 */     ComponentSerialization.TRUSTED_STREAM_CODEC.encode(output, this.playerSuffix);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundSetPlayerTeamPacket$Parameters.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */