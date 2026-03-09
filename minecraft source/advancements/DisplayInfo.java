/*     */ package net.minecraft.advancements;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function8;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.ClientAsset;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ public class DisplayInfo {
/*  15 */   public static final Codec<DisplayInfo> CODEC = RecordCodecBuilder.create(i -> i.group(ItemStack.STRICT_CODEC
/*  16 */         .fieldOf("icon").forGetter(DisplayInfo::getIcon), ComponentSerialization.CODEC
/*  17 */         .fieldOf("title").forGetter(DisplayInfo::getTitle), ComponentSerialization.CODEC
/*  18 */         .fieldOf("description").forGetter(DisplayInfo::getDescription), ClientAsset.ResourceTexture.CODEC
/*  19 */         .optionalFieldOf("background").forGetter(DisplayInfo::getBackground), AdvancementType.CODEC
/*  20 */         .optionalFieldOf("frame", AdvancementType.TASK).forGetter(DisplayInfo::getType), Codec.BOOL
/*  21 */         .optionalFieldOf("show_toast", Boolean.valueOf(true)).forGetter(DisplayInfo::shouldShowToast), Codec.BOOL
/*  22 */         .optionalFieldOf("announce_to_chat", Boolean.valueOf(true)).forGetter(DisplayInfo::shouldAnnounceChat), Codec.BOOL
/*  23 */         .optionalFieldOf("hidden", Boolean.valueOf(false)).forGetter(DisplayInfo::isHidden))
/*  24 */       .apply(i, DisplayInfo::new));
/*     */   
/*  26 */   public static final StreamCodec<RegistryFriendlyByteBuf, DisplayInfo> STREAM_CODEC = StreamCodec.ofMember(DisplayInfo::serializeToNetwork, DisplayInfo::fromNetwork);
/*     */   
/*     */   private final Component title;
/*     */   private final Component description;
/*     */   private final ItemStack icon;
/*     */   private final Optional<ClientAsset.ResourceTexture> background;
/*     */   private final AdvancementType type;
/*     */   private final boolean showToast;
/*     */   private final boolean announceChat;
/*     */   private final boolean hidden;
/*     */   private float x;
/*     */   private float y;
/*     */   
/*     */   public DisplayInfo(ItemStack icon, Component title, Component description, Optional<ClientAsset.ResourceTexture> background, AdvancementType type, boolean showToast, boolean announceChat, boolean hidden) {
/*  40 */     this.title = title;
/*  41 */     this.description = description;
/*  42 */     this.icon = icon;
/*  43 */     this.background = background;
/*  44 */     this.type = type;
/*  45 */     this.showToast = showToast;
/*  46 */     this.announceChat = announceChat;
/*  47 */     this.hidden = hidden;
/*     */   }
/*     */   
/*     */   public void setLocation(float x, float y) {
/*  51 */     this.x = x;
/*  52 */     this.y = y;
/*     */   }
/*     */ 
/*     */   
/*  56 */   public Component getTitle() { return this.title; }
/*     */ 
/*     */ 
/*     */   
/*  60 */   public Component getDescription() { return this.description; }
/*     */ 
/*     */ 
/*     */   
/*  64 */   public ItemStack getIcon() { return this.icon; }
/*     */ 
/*     */ 
/*     */   
/*  68 */   public Optional<ClientAsset.ResourceTexture> getBackground() { return this.background; }
/*     */ 
/*     */ 
/*     */   
/*  72 */   public AdvancementType getType() { return this.type; }
/*     */ 
/*     */ 
/*     */   
/*  76 */   public float getX() { return this.x; }
/*     */ 
/*     */ 
/*     */   
/*  80 */   public float getY() { return this.y; }
/*     */ 
/*     */ 
/*     */   
/*  84 */   public boolean shouldShowToast() { return this.showToast; }
/*     */ 
/*     */ 
/*     */   
/*  88 */   public boolean shouldAnnounceChat() { return this.announceChat; }
/*     */ 
/*     */ 
/*     */   
/*  92 */   public boolean isHidden() { return this.hidden; }
/*     */ 
/*     */   
/*     */   private void serializeToNetwork(RegistryFriendlyByteBuf output) {
/*  96 */     ComponentSerialization.TRUSTED_STREAM_CODEC.encode(output, this.title);
/*  97 */     ComponentSerialization.TRUSTED_STREAM_CODEC.encode(output, this.description);
/*  98 */     ItemStack.STREAM_CODEC.encode(output, this.icon);
/*  99 */     output.writeEnum(this.type);
/* 100 */     int flags = 0;
/* 101 */     if (this.background.isPresent()) {
/* 102 */       flags |= 0x1;
/*     */     }
/* 104 */     if (this.showToast) {
/* 105 */       flags |= 0x2;
/*     */     }
/* 107 */     if (this.hidden) {
/* 108 */       flags |= 0x4;
/*     */     }
/* 110 */     output.writeInt(flags);
/* 111 */     Objects.requireNonNull(output); this.background.map(ClientAsset::id).ifPresent(output::writeIdentifier);
/* 112 */     output.writeFloat(this.x);
/* 113 */     output.writeFloat(this.y);
/*     */   }
/*     */   
/*     */   private static DisplayInfo fromNetwork(RegistryFriendlyByteBuf input) {
/* 117 */     Component title = (Component)ComponentSerialization.TRUSTED_STREAM_CODEC.decode(input);
/* 118 */     Component description = (Component)ComponentSerialization.TRUSTED_STREAM_CODEC.decode(input);
/* 119 */     ItemStack icon = (ItemStack)ItemStack.STREAM_CODEC.decode(input);
/* 120 */     AdvancementType frame = (AdvancementType)input.readEnum(AdvancementType.class);
/* 121 */     int flags = input.readInt();
/* 122 */     Optional<ClientAsset.ResourceTexture> background = ((flags & true) != 0) ? Optional.of(new ClientAsset.ResourceTexture(input.readIdentifier())) : Optional.empty();
/* 123 */     boolean showToast = ((flags & 0x2) != 0);
/* 124 */     boolean hidden = ((flags & 0x4) != 0);
/* 125 */     DisplayInfo info = new DisplayInfo(icon, title, description, background, frame, showToast, false, hidden);
/* 126 */     info.setLocation(input.readFloat(), input.readFloat());
/* 127 */     return info;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\DisplayInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */