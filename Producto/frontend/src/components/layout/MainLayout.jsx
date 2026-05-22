import { useState } from 'react';
import Sidebar from '@layouts/Sidebar';
import Navbar  from '@layouts/Navbar';

export default function MainLayout({ children }) {
  const [isSidebarOpen, setIsSidebarOpen] = useState(true);

  return (
    <div className="flex bg-gray-100 min-h-screen">

      <Sidebar isOpen={isSidebarOpen} setIsOpen={setIsSidebarOpen} />

      <div className={`flex-1 transition-all duration-300 ${isSidebarOpen ? 'md:ml-64' : 'md:ml-20'} ml-0 flex flex-col min-h-screen overflow-hidden`}>

        <Navbar isSidebarOpen={isSidebarOpen} />

        <main className="mt-20 p-8 flex-1 animate-in fade-in duration-500 overflow-auto">
          {children}
        </main>

      </div>
    </div>
  );
}
