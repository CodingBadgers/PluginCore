package uk.codingbadgers.plugincore.modules;

import java.net.URL;
import java.net.URLClassLoader;

public class ModuleClassLoader extends URLClassLoader {
    private final ModuleLoader m_loader;

    public ModuleClassLoader(ModuleLoader loader, URL url, ClassLoader parent) {
        super(new URL[]{url}, parent);

        m_loader = loader;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            return super.findClass(name);
        } catch (ClassNotFoundException ex) {
            return m_loader.findClass(name);
        }
    }
}
