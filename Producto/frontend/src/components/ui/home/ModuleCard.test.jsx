import { render, screen, fireEvent } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import ModuleCard from './ModuleCard';
import { Layout } from 'lucide-react';

describe('ModuleCard Component', () => {
    const mockModule = {
        title: 'Test Module',
        description: 'Test Description',
        icon: Layout,
        color: 'from-blue-500 to-blue-600'
    };

    it('renders module title and description', () => {
        render(<ModuleCard module={mockModule} onClick={() => {}} />);
        
        expect(screen.getByText('Test Module')).toBeInTheDocument();
        expect(screen.getByText('Test Description')).toBeInTheDocument();
    });

    it('calls onClick when clicked', () => {
        const handleClick = vi.fn();
        render(<ModuleCard module={mockModule} onClick={handleClick} />);
        
        fireEvent.click(screen.getByText('Test Module').closest('div'));
        expect(handleClick).toHaveBeenCalledTimes(1);
    });
});
