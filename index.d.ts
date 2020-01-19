import * as React from 'react';
import {ViewProps} from 'react-native';

declare class Insets {
    leftInset: number;
    topInset: number;
    rightInset: number;
    bottomInset: number;
}

export interface WindowGuardProps extends ViewProps {
    applyInsets: string[]
    onInsetsChange?: (insets: Insets, notch: boolean) => void
}

export default class WindowGuard extends React.Component<WindowGuardProps> {
    static bottom: string[];
    static left: string[];
    static right: string[];
    static top: string[];
    static horizontal: string[];
    static vertical: string[];
    static all: string[];

    adjustInsets(): void
}

export function withWindowGuard(component: React.ReactNode, insetsConfig: string[]): React.Component;

