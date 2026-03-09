package net.minecraft.util.debug;

public interface Registration {
  <T> void register(DebugSubscription<T> paramDebugSubscription, DebugValueSource.ValueGetter<T> paramValueGetter);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\debug\DebugValueSource$Registration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */